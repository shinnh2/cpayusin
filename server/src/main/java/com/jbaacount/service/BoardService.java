package com.jbaacount.service;

import com.jbaacount.payload.request.BoardPatchDto;
import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponseDto;
import com.jbaacount.model.Board;
import com.jbaacount.repository.BoardRepository;
import com.jbaacount.payload.request.CategoryPatchDto;
import com.jbaacount.model.Category;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
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
    private final CategoryService categoryService;
    private final AuthorizationService authorizationService;
    private final FileService fileService;
    private final VoteService voteService;

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
            //boardId, orderIndex = 필수
            //name, isAdminOnly, category = 있으면 변경
            Board board = getBoardById(request.getBoardId());

            log.info("board = {}", board.getName());

            if(request.getIsDeleted() != null)
            {
                deleteBoard(board.getId());
                continue;
            }

            board.updateOrderIndex(request.getOrderIndex());
            Optional.ofNullable(request.getName())
                            .ifPresent(boardName -> board.updateName(boardName));
            Optional.ofNullable(request.getIsAdminOnly())
                            .ifPresent(isAdminOnly -> board.changeBoardAuthority(isAdminOnly));

            if(request.getCategory() != null && !request.getCategory().isEmpty())
            {
                //orderIndex, categoryId = 필수
                //name, isAdminOnly = 있으면 변경
                List<CategoryPatchDto> categoryPatchList = request.getCategory();
                for(CategoryPatchDto categoryRequest : categoryPatchList)
                {
                    Long categoryId = categoryRequest.getCategoryId();
                    Category category = categoryService.getCategory(categoryId);

                    category.addBoard(board);
                    category.updateOrderIndex(categoryRequest.getOrderIndex());

                    if(categoryRequest.getIsDeleted() != null)
                    {
                        deleteCategory(categoryId);
                        continue;
                    }

                    Optional.ofNullable(categoryRequest.getName())
                            .ifPresent(categoryName -> category.updateName(categoryName));

                    Optional.ofNullable(categoryRequest.getIsAdminOnly())
                            .ifPresent(isAdminOnly -> category.changeCategoryAuthority(isAdminOnly));
                }
            }
        }

        log.info("업데이트 종료");
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

    public void deleteBoard(Long boardId)
    {
        Board board = getBoardById(boardId);
        List<Category> categories = board.getCategories();
        for (Category category : categories)
        {
            List<Post> posts = category.getPosts();
            for (Post post : posts)
            {
                log.info("post removed = {}", post.getTitle());
                log.info("file removed in board service");

                voteService.deleteVoteByPostId(post.getId());
                fileService.deleteUploadedFile(post);
            }
        }

        boardRepository.deleteById(boardId);
    }

    public void deleteCategory(Long categoryId)
    {
        Category category = categoryService.getCategory(categoryId);
        List<Post> posts = category.getPosts();
        for (Post post : posts)
        {
            log.info("post removed = {}", post.getTitle());
            log.info("file removed in category service");

            voteService.deleteVoteByPostId(post.getId());
            fileService.deleteUploadedFile(post);
        }

    }
}
