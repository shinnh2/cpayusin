/*
package com.jbaacount.post.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.dto.request.PostPatchDto;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void beforeEach()
    {
        Member user = Member
                .builder()
                .email("aaa@naver.com")
                .password("123123")
                .nickname("일반유저")
                .build();

        Member admin = Member
                .builder()
                .email("mike@ticonsys.com")
                .password("123123")
                .nickname("관리자")
                .build();

        memberService.createMember(user);
        memberService.createMember(admin);

        Post post1 = Post
                .builder()
                .title("제목 테스트용1")
                .content("내용 테스트용1")
                .build();

        Post post2 = Post
                .builder()
                .title("제목 테스트용2")
                .content("내용 테스트용2")
                .build();

        postService.createPost(post1, user);
        postService.createPost(post2, admin);

    }

    @AfterEach
    void afterEach()
    {
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void createPostTest()
    {
        Member user = memberRepository.findByNickname("일반유저").get();

        String title = "첫번째게시물";
        String content = "내용";

        Post post = Post
                .builder()
                .title(title)
                .content(content)
                .build();

        Post savedPost = postService.createPost(post, user);

        assertThat(savedPost.getTitle()).isEqualTo("첫번째게시물");
        assertThat(savedPost.getContent()).isEqualTo("내용");
        assertThat(savedPost.getMember().getEmail()).isEqualTo("aaa@naver.com");
        assertThat(savedPost.getMember().getNickname()).isEqualTo("일반유저");
        assertThat(savedPost.getMember().getRoles()).contains("USER");
    }

    @Test
    void updatePost_WithAuthorizedUser()
    {
        Member user = memberRepository.findByNickname("일반유저").get();
        Post userPost = user.getPosts().get(0);

        PostPatchDto patchDto = new PostPatchDto();
        patchDto.setTitle("제목 수정 테스트 1");
        Post updatedPost = postService.updatePost(userPost.getId(), patchDto, user);

        assertThat(updatedPost.getTitle()).isEqualTo("제목 수정 테스트 1");
        assertThat(updatedPost.getMember()).isEqualTo(user);
    }

    @Test
    void updatePost_WithDifferentUser()
    {
        Member user = memberRepository.findByNickname("일반유저").get();
        Member admin = memberRepository.findByNickname("관리자").get();

        Post adminPost = postRepository.findByTitle("2").get();
        PostPatchDto patchDto = new PostPatchDto();
        patchDto.setTitle("제목 수정 테스트 2");

        assertThrows(BusinessLogicException.class, () -> postService.updatePost(adminPost.getId(), patchDto, user));
    }

    @Test
    void deletePost_WithAuthorizedUser()
    {
        Member user = memberRepository.findByNickname("일반유저").get();
        Member admin = memberRepository.findByNickname("관리자").get();

        Post userPost = user.getPosts().get(0);
        postService.deletePostById(userPost.getId(), user);
        em.flush();
        em.refresh(user);

        assertTrue(user.getPosts().isEmpty());
    }

    @Test
    void deletePost_WithUnauthorizedUser()
    {
        Member user = memberRepository.findByNickname("일반유저").get();
        Member admin = memberRepository.findByNickname("관리자").get();

        Post userPost = user.getPosts().get(0);

        postService.deletePostById(userPost.getId(), admin);

        em.flush();
        em.refresh(user);

        assertTrue(user.getPosts().isEmpty());
    }

    @Test
    void deletePost_WithAdmin()
    {
        Member user = memberRepository.findByNickname("일반유저").get();
        Member admin = memberRepository.findByNickname("관리자").get();

        Post adminPost = admin.getPosts().get(0);

        assertThrows(BusinessLogicException.class, () -> postService.deletePostById(adminPost.getId(), user));
    }

}*/
