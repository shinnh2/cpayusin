package com.jbaacount.post.service;

import com.jbaacount.category.entity.Category;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.service.MemberService;
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
    private final CategoryService categoryService;

    public Post createPost(Post request, Long categoryId, Member currentMember)
    {
        Category category = categoryService.getCategory(categoryId);
        authorizationService.isUserAllowed(category.isAdminOnly());

        Post savedPost = postRepository.save(request);
        savedPost.addMember(currentMember);
        savedPost.addCategory(category);

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
}
