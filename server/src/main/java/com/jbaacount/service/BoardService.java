package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.mapper.BoardMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.type.BoardType;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.response.BoardChildrenResponse;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.BoardTypeResponse;
import com.jbaacount.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
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

        BoardResponse response = BoardMapper.INSTANCE.boardToResponse(boardRepository.save(board));
        return response;
    }


    @Transactional
    public void bulkUpdateBoards(List<BoardUpdateRequest> requests, Member currentMember)
    {
        utilService.isAdmin(currentMember);

        requests.forEach(request ->
                {
                    Board board = getBoardById(request.getId());
                    if(request.getIsDeleted() != null || request.getIsDeleted().equals(true))
                        boardRepository.delete(board);

                    BoardMapper.INSTANCE.updateBoard(request, board);
                    board.setType(BoardType.BOARD.getCode());

                    if(request.getCategory() != null & !request.getCategory().isEmpty())
                    {
                        request.getCategory().forEach(categoryRequest -> {
                            Board category = getBoardById(categoryRequest.getId());

                            if(categoryRequest.getIsDeleted() != null || categoryRequest.getIsDeleted().equals(true))
                                boardRepository.delete(category);

                            else {
                                BoardMapper.INSTANCE.updateBoard(categoryRequest, board);
                                category.setType(BoardType.CATEGORY.getCode());
                                category.addParent(board);
                            }
                        });
                    }
                }
        );

        log.info("업데이트 종료");
    }

    public Board getBoardById(Long boardId)
    {
        return boardRepository.findById(boardId)
                .orElseThrow();
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

    public List<BoardTypeResponse> getBoardType()
    {
        return boardRepository.findBoardType();
    }


    public List<BoardMenuResponse> getMenuList()
    {
        List<Board> result = boardRepository.findAll();

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

}
