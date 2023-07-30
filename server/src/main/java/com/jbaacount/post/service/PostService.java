package com.jbaacount.post.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.dto.request.PostPatchDto;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService
{
    private final PostRepository postRepository;
    private final AuthorizationService authorizationService;
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    public Post createPost(Post request, Long categoryId, Long boardId, Member currentMember)
    {
        Board board = getBoard(boardId);
        Post savedPost = postRepository.save(request);

        authorizationService.isUserAllowed(board.getIsAdminOnly(), currentMember);

        if(categoryId != null)
        {
            Category category = getCategory(categoryId);
            checkBoardHasCategory(board, category);

            authorizationService.isUserAllowed(category.getIsAdminOnly(), currentMember);
            savedPost.addCategory(category);
        }

        log.info("===postService - createPost===");
        log.info("post saved successfully = {}", savedPost.getTitle());

        savedPost.addMember(currentMember);
        savedPost.addBoard(board);

        return savedPost;
    }

    public Post updatePost(Long postId, PostPatchDto request, Member currentMember)
    {
        Post post = getPostById(postId);
        //Only the owner of the post has the authority to update
        authorizationService.isTheSameUser(post.getMember().getId(), currentMember.getId());

        Optional.ofNullable(request.getTitle())
                .ifPresent(title -> post.updateTitle(title));
        Optional.ofNullable(request.getContent())
                .ifPresent(content -> post.updateContent(content));

        Optional.ofNullable(request.getCategoryId())
                .ifPresent(categoryId ->
                {
                    Category category = getCategory(categoryId);
                    authorizationService.isUserAllowed(category.getIsAdminOnly(), currentMember);
                    Board board = category.getBoard();

                    post.addCategory(category);
                    post.addBoard(board);
                });

        return post;
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }

    public void deletePostById(Long postId, Member currentMember)
    {
        Post post = getPostById(postId);
        authorizationService.checkPermission(post.getMember().getId(), currentMember);

        postRepository.deleteById(postId);
    }

    private Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    private Board getBoard(Long boardId)
    {
        return boardRepository.findById(boardId).orElseThrow();
    }

    private void checkBoardHasCategory(Board board, Category category)
    {
        if(!board.getCategories().contains(category))
            throw new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND);
    }
}
