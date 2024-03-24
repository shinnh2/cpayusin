package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.mapper.BoardMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.model.type.BoardType;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.request.CategoryUpdateRequest;
import com.jbaacount.payload.response.BoardChildrenResponse;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class BoardService
{
    private final BoardRepository boardRepository;
    private final UtilService utilService;
    private final FileService fileService;
    private final VoteService voteService;

    @Transactional
    public BoardResponse createBoard(BoardCreateRequest request, Member currentMember)
    {
        utilService.isAdmin(currentMember);

        Board board = BoardMapper.INSTANCE.toBoardEntity(request);
        if (request.getParentId() != null)
        {
            Board parent = getBoardById(request.getParentId());
            if (parent.getParent() != null)
                throw new BusinessLogicException(ExceptionMessage.BOARD_TYPE_ERROR);

            board.addParent(parent);
            Integer orderIndex = boardRepository.countChildrenByParentId(parent.getId());
            board.updateOrderIndex(orderIndex + 1);
            board.updateBoardType(BoardType.CATEGORY.getCode());
        } else
        {
            Integer orderIndex = boardRepository.countParent();
            board.updateOrderIndex(orderIndex + 1);
        }

        return BoardMapper.INSTANCE.boardToResponse(boardRepository.save(board));
    }


    @Transactional
    public List<BoardMenuResponse> bulkUpdateBoards(List<BoardUpdateRequest> requests, Member currentMember)
    {
        utilService.isAdmin(currentMember);

        for(BoardUpdateRequest request : requests)
        {
            Board board = getBoardById(request.getId());
            if(request.getIsDeleted() != null && request.getIsDeleted())
                deleteBoard(board);

            else{
                BoardMapper.INSTANCE.updateBoard(request, board);
                board.setParent(null);
                board.setType(BoardType.BOARD.getCode());

                if(!request.getCategory().isEmpty())
                    updateCategory(board, request.getCategory());
            }
        }

        log.info("업데이트 종료");
        return getMenuList();
    }

    public void updateCategory(Board parent, List<CategoryUpdateRequest> requests)
    {

        for(CategoryUpdateRequest request : requests)
        {
            Board category = getBoardById(request.getId());

            if(request.getIsDeleted() != null && request.getIsDeleted())
                deleteBoard(category);

            else{
                BoardMapper.INSTANCE.updateBoard(request, category);
                category.setType(BoardType.CATEGORY.getCode());

                category.addParent(parent);
            }
        }
    }

    public Board getBoardById(Long boardId)
    {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.BOARD_NOT_FOUND));
    }

    public BoardResponse findBoardById(Long boardId)
    {
        return BoardMapper.INSTANCE.boardToResponse(getBoardById(boardId));
    }

    public List<BoardResponse> findCategoryByBoardId(Long boardId)
    {
        var result = boardRepository.findBoardByParentBoardId(boardId);

        return BoardMapper.INSTANCE.toBoardResponseList(result);
    }

    public List<BoardMenuResponse> getMenuList()
    {
        List<Board> result = boardRepository.findAll();

        if(result.isEmpty())
            return Collections.emptyList();

        List<BoardMenuResponse> boardList = BoardMapper.INSTANCE.toBoardMenuResponse(result.stream()
                .filter(board -> board.getType().equals(BoardType.BOARD.getCode()))
                .sorted(Comparator.comparingInt(Board::getOrderIndex))
                .collect(Collectors.toList()));

        List<BoardChildrenResponse> categoryList = BoardMapper.INSTANCE.toChildrenList(result.stream()
                .filter(board -> board.getType().equals(BoardType.CATEGORY.getCode()))
                .sorted(Comparator.comparingInt(Board::getOrderIndex))
                .collect(Collectors.toList()));


        boardList.forEach(board -> board.setCategory(
                categoryList.stream()
                        .filter(category -> category.getParentId().equals(board.getId()))
                        .collect(Collectors.toList())));

        return boardList;
    }

    public List<Long> getBoardIdListByParentId(Long boardId)
    {
        return boardRepository.findBoardIdListByParentId(boardId);
    }


    private void deleteBoard(Board board)
    {
        if(!board.getPosts().isEmpty())
        {
            for(Post post : board.getPosts())
            {
                fileService.deleteUploadedFile(post);
                voteService.deleteVoteByPostId(post.getId());
            }
        }
        boardRepository.delete(board);
    }

}
