package com.jbaacount.post.controller;

import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.dto.request.PostPatchDto;
import com.jbaacount.post.dto.request.PostPostDto;
import com.jbaacount.post.dto.response.PostMultiResponseDto;
import com.jbaacount.post.dto.response.PostSingleResponseDto;
import com.jbaacount.post.dto.response.PostResponseForProfile;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.mapper.PostMapper;
import com.jbaacount.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class PostController
{
    private final PostService postService;
    private final PostMapper postMapper;
    private final MemberService memberService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity savePost(@RequestPart(value = "data") @Valid PostPostDto request,
                                   @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                   @AuthenticationPrincipal Member currentMember)
    {
        Long categoryId = request.getCategoryId();
        Long boardId = request.getBoardId();
        Member member = memberService.getMemberById(currentMember.getId());

        log.info("category id = {}", categoryId);
        log.info("board Id = {}", boardId);

        Post post = postMapper.postDtoToPostEntity(request);
        Post savedPost = postService.createPost(post, files, categoryId, boardId, member);
        PostSingleResponseDto response = postMapper.postEntityToResponse(savedPost, member);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity updatePost(@RequestPart(value = "data") @Valid PostPatchDto request,
                                     @PathVariable("post-id") @Positive Long postId,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @AuthenticationPrincipal Member currentMember)
    {
        Post post = postService.updatePost(postId, request, files, currentMember);
        PostSingleResponseDto response = postMapper.postEntityToResponse(post, currentMember);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive Long postId,
                                  @AuthenticationPrincipal Member currentMember)
    {
        PostSingleResponseDto response = postMapper.postEntityToResponse(postService.getPostById(postId), currentMember);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/category/{category-id}/posts")
    public ResponseEntity getAllPostsByCategoryId(@PathVariable("category-id") @Positive Long categoryId,
                                                @RequestParam(required = false) Long post,
                                                @RequestParam(required = false) String keyword,
                                                @PageableDefault(size = 8) Pageable pageable)
    {
        SliceDto<PostMultiResponseDto> response = postService.getAllPostsByCategoryId(categoryId, keyword, post, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/board/{board-id}/posts")
    public ResponseEntity getAllPostsByBoardId(@PathVariable("board-id") @Positive Long boardId,
                                                  @RequestParam(required = false) Long post,
                                                  @RequestParam(required = false) String keyword,
                                                  @PageableDefault(size = 8) Pageable pageable)
    {
        SliceDto<PostMultiResponseDto> response = postService.getAllPostsByBoardId(boardId, keyword, post, pageable);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile/{member-id}/posts")
    public ResponseEntity getAllPostsByMemberId(@PathVariable("member-id") @Positive Long memberId,
                                                @RequestParam(required = false) Long post,
                                                @PageableDefault(size = 8) Pageable pageable)
    {
        SliceDto<PostResponseForProfile> response = postService.getAllPostsByMemberId(memberId, post, pageable);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePost(@PathVariable("post-id") @Positive Long postId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        postService.deletePostById(postId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
