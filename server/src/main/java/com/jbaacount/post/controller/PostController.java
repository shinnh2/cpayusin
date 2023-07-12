package com.jbaacount.post.controller;

import com.jbaacount.member.entity.Member;
import com.jbaacount.post.dto.request.PostPostDto;
import com.jbaacount.post.dto.response.PostResponseDto;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.mapper.PostMapper;
import com.jbaacount.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
