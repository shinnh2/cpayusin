package com.jbaacount.post.controller;

import com.jbaacount.member.entity.Member;
import com.jbaacount.post.dto.request.PostPatchDto;
import com.jbaacount.post.dto.request.PostPostDto;
import com.jbaacount.post.dto.response.PostResponseDto;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.mapper.PostMapper;
import com.jbaacount.post.service.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class PostController
{
    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity savePost(@RequestBody PostPostDto request,
                                   @AuthenticationPrincipal Member currentMember)
    {
        Post post = postMapper.postDtoToPostEntity(request);
        PostResponseDto response = postMapper.postEntityToResponse(postService.createPost(post, currentMember));

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity updatePost(@RequestBody PostPatchDto request,
                                     @PathVariable("post-id") @Positive Long postId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        Post post = postService.updatePost(postId, request, currentMember);
        PostResponseDto response = postMapper.postEntityToResponse(post);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive Long postId)
    {
        PostResponseDto response = postMapper.postEntityToResponse(postService.getPostById(postId));

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePost(@PathVariable("post-id") @Positive Long postId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        postService.deletePostById(postId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
