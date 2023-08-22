package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
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
import java.util.Optional;

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

        Board savedBoard = boardRepository.save(board);
        savedBoard.updateOrderIndex(savedBoard.getId());

        return savedBoard;
    }

    public Board updateBoard(Long boardId, BoardPatchDto request, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Board board = getBoardById(boardId);

        Optional.ofNullable(request.getName())
                .ifPresent(name -> board.updateName(name));

        Optional.ofNullable(request.getIsAdminOnly())
                .ifPresent(authority -> board.changeBoardAuthority(authority));

        Optional.ofNullable(request.getOrderIndex())
                .ifPresent(orderIndex ->{
                    Long currentIndex = board.getOrderIndex();
                    if(currentIndex > orderIndex)
                    {
                        List<Board> allBoards = boardRepository.findAllBetween(orderIndex, currentIndex);
                        for (Board boardList : allBoards)
                        {
                            boardList.updateOrderIndex(boardList.getOrderIndex() + 1);
                        }
                    }

                    else
                    {
                        List<Board> allBoards = boardRepository.findAllBetween(currentIndex, orderIndex);
                        for (Board boardList : allBoards)
                        {
                            boardList.updateOrderIndex(boardList.getOrderIndex() - 1);
                        }
                    }
                    board.updateOrderIndex(orderIndex);
                });

        return board;
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

}
