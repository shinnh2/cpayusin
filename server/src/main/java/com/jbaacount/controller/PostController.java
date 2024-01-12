package com.jbaacount.controller;

import com.jbaacount.global.dto.PageInfo;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.request.PostUpdateRequest;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.payload.response.PostSingleResponse;
import com.jbaacount.service.MemberService;
import com.jbaacount.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@RestController
public class PostController
{
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping(value = "/post/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse<PostSingleResponse>> savePost(@RequestPart(value = "data") @Valid PostCreateRequest request,
                                                                       @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                       @AuthenticationPrincipal Member currentMember,
                                                                       @RequestParam("board") Long boardId,
                                                                       @RequestParam(value = "category", required = false) Long categoryId)
    {
        Member member = memberService.getMemberById(currentMember.getId());

        var data = postService.createPost(request, files, categoryId, boardId, member);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/post/update/{post-id}")
    public ResponseEntity updatePost(@RequestPart(value = "data") @Valid PostUpdateRequest request,
                                     @PathVariable("post-id") @Positive Long postId,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @AuthenticationPrincipal Member currentMember)
    {
        var data = postService.updatePost(postId, request, files, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/post/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive Long postId,
                                  @AuthenticationPrincipal Member currentMember)
    {
        var data = postService.getPostSingleResponse(postId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/profile/my-posts")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity getMyPosts(@AuthenticationPrincipal Member member,
                                     @PageableDefault Pageable pageable)
    {
        var data = postService.getMyPosts(member, pageable.previousOrFirst());

        return ResponseEntity.ok(new GlobalResponse<>(data.getContent(), PageInfo.of(data)));
    }

    @GetMapping("/post/board")
    public ResponseEntity getAllByBoardId(@PageableDefault Pageable pageable,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam("id") Long boardId)
    {
        var data = postService.getPostsByBoardId(boardId, keyword, pageable.previousOrFirst());

        return ResponseEntity.ok(new GlobalResponse<>(data.getContent(), PageInfo.of(data)));
    }

    @GetMapping("/post/category")
    public ResponseEntity getPostsByCategoryId(@PageableDefault Pageable pageable,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam("id") Long categoryId)
    {
        var data = postService.getPostsByCategoryId(categoryId, keyword, pageable.previousOrFirst());

        return ResponseEntity.ok(new GlobalResponse<>(data.getContent(), PageInfo.of(data)));
    }


    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<GlobalResponse<String>> deletePost(@PathVariable("post-id") @Positive Long postId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        postService.deletePostById(postId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>("삭제가 완료되었습니다."));
    }
}
