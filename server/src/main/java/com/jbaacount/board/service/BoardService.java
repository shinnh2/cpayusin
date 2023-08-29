package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.response.BoardAndCategoryResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.category.entity.Category;
import com.jbaacount.file.service.FileService;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class BoardService
{
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;
    private final FileService fileService;
    private final VoteRepository voteRepository;

    public Board createBoard(Board board, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        long orderIndex = boardRepository.countBoard();
        board.updateOrderIndex(orderIndex + 1);

        return boardRepository.save(board);
    }

    public void bulkUpdateBoards(List<BoardPatchDto> requests, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        for(BoardPatchDto request : requests)
        {
            if(Boolean.TRUE.equals(request.getIsDeleted()))
            {
                Board board = getBoardById(request.getBoardId());
                log.info("delete process = {}", board.getOrderIndex());
                processDelete(request, board, currentMember);
            }
        }

        List<Long> boardIds = requests.stream()
                .filter(request -> request.getIsDeleted() == null || !request.getIsDeleted())
                .map(BoardPatchDto::getBoardId)
                .collect(toList());

        List<Board> boardList = boardRepository.findAllById(boardIds);

        Map<Long, BoardPatchDto> boardMap = requests.stream()
                .collect(toMap(BoardPatchDto::getBoardId, Function.identity()));

        for(Board board : boardList)
        {
            BoardPatchDto request = boardMap.get(board.getId());
            processUpdateBoard(request, board);
        }
    }


    @Transactional(readOnly = true)
    public Board getBoardById(Long boardId)
    {
        return boardRepository.findById(boardId)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getAllBoards()
    {
        return boardRepository.findAllBoards();
    }

    @Transactional(readOnly = true)
    public List<BoardAndCategoryResponse> getAllBoardAndCategory()
    {
        return boardRepository.findAllBoardAndCategory();
    }

    public void deleteBoard(Long boardId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Board board = getBoardById(boardId);
        List<Category> categories = board.getCategories();
        for (Category category : categories)
        {
            List<Post> posts = category.getPosts();
            for (Post post : posts)
            {
                log.info("post removed = {}", post.getTitle());
                log.info("file removed in board service");

                voteRepository.deleteByPostId(post.getId());
                fileService.deleteUploadedFile(post);
            }
        }

        boardRepository.deleteById(boardId);
    }

    private void processDelete(BoardPatchDto request, Board board, Member currentMember)
    {
        Optional.ofNullable(request.getIsDeleted())
                .ifPresent(isDeleted -> {
                    Long lastOrderIndex = boardRepository.findTheBiggestOrderIndex();
                    Long startOrderIndex = board.getOrderIndex();



                    if(startOrderIndex < lastOrderIndex)
                    {
                        log.info("update orderIndex between = {} and {}", startOrderIndex, lastOrderIndex);
                        boardRepository.bulkUpdateOrderIndex(startOrderIndex + 1, lastOrderIndex, -1);
                    }

                    deleteBoard(board.getId(), currentMember);
                });
    }

    private void processUpdateBoard(BoardPatchDto request, Board board)
    {
        Optional.ofNullable(request.getName())
                .ifPresent(name -> board.updateName(name));
        Optional.ofNullable(request.getIsAdminOnly())
                .ifPresent(authority -> board.changeBoardAuthority(authority));
        updateBoardOrderIndex(request, board);
    }

    private void updateBoardOrderIndex(BoardPatchDto request, Board board)
    {
        Optional.ofNullable(request.getOrderIndex())
                .ifPresent(orderIndex ->{
                    Long currentIndex = board.getOrderIndex();

                    if(currentIndex > orderIndex)
                        boardRepository.bulkUpdateOrderIndex(orderIndex, currentIndex, 1);

                    else
                        boardRepository.bulkUpdateOrderIndex(currentIndex, orderIndex, -1);

                    board.updateOrderIndex(orderIndex);
                });
    }

}
