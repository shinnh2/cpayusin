package com.jbaacount.post.service;

import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @BeforeAll
    public static void beforeAll()
    {

    }

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
                .email("ge0nmo@naver.com")
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
        postRepository.deleteAll();
        memberRepository.deleteAll();
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

}