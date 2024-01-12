package com.jbaacount.service;

import com.jbaacount.mapper.BoardMapper;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.model.Board;
import com.jbaacount.repository.BoardRepository;
import com.jbaacount.payload.request.CategoryUpdateRequest;
import com.jbaacount.model.Category;
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
@Transactional(readOnly = true)
@Service
public class BoardService
{
    private final BoardRepository boardRepository;
    private final CategoryService categoryService;
    private final AuthorizationService authorizationService;
    private final FileService fileService;
    private final VoteService voteService;

    public BoardResponse createBoard(BoardCreateRequest request, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Board board = BoardMapper.INSTANCE.toBoardEntity(request);

        long orderIndex = boardRepository.countBoard();
        board.updateOrderIndex(orderIndex + 1);

        return BoardMapper.INSTANCE.boardToResponse(boardRepository.save(board));
    }


    @Transactional
    public void bulkUpdateBoards(List<BoardUpdateRequest> requests, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        for(BoardUpdateRequest request : requests)
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
                List<CategoryUpdateRequest> categoryPatchList = request.getCategory();
                for(CategoryUpdateRequest categoryRequest : categoryPatchList)
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


    public Board getBoardById(Long boardId)
    {
        return boardRepository.findById(boardId)
                .orElseThrow();
    }

    public BoardResponse getBoardResponse(Long boardId)
    {
        Board board = getBoardById(boardId);

        return BoardMapper.INSTANCE.boardToResponse(board);
    }


    public List<BoardResponse> getAllBoards()
    {
        return boardRepository.findAllBoards();
    }


    public List<BoardAndCategoryResponse> getAllBoardAndCategory()
    {
        return boardRepository.findAllBoardAndCategory();
    }

    @Transactional
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

    @Transactional
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
