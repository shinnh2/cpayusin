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
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Category category = getCategory(categoryId);
        Board board = getBoard(boardId);

        authorizationService.isUserAllowed(category.getIsAdminOnly(), currentMember);

        Post savedPost = postRepository.save(request);
        savedPost.addMember(currentMember);
        savedPost.addCategory(category);
        savedPost.addBoard(board);

        log.info("===createPost in service===");
        log.info("board id = {}", board.getId());

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
                    post.addCategory(category);
                });

        return post;
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }

    public Page<PostInfoForResponse> getPostInfoForCategory(Long categoryId, Pageable pageable)
    {
        return postRepository.getAllPostsForCategory(categoryId, pageable);
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
}
