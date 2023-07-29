/*
package com.jbaacount.vote.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import com.jbaacount.post.service.PostService;
import com.jbaacount.utils.TestUtil;
import com.jbaacount.vote.repository.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class VoteFacadeTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteFacade voteFacade;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;

    private static final String boardName = "첫번째 게시판";
    private static final String post1Title = "게시판1";


    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);


        Board board1 = Board.builder()
                .name(boardName)
                .isAdminOnly(false)
                .build();


        boardService.createBoard(board1, admin);

        Post post = Post
                .builder()
                .title(post1Title)
                .content("내용 테스트용1")
                .build();


        postService.createPost(post, null, board1.getId(), admin);
    }

    @Test
    void vote_multiThread() throws InterruptedException
    {
        Post post = getPost();
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++)
        {
            String nickname = "user" + i;
            String email = "email@com"+ i;
            String password = "123"+ i;

            Member member = memberService.createMember(Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build());

            executorService.submit(() -> {
                try{
                    voteFacade.votePost(member, post.getId());
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                } finally
                {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        long count = voteRepository.count();

        Assertions.assertThat(count).isEqualTo(100);
    }

    private Post getPost()
    {
        return postRepository.findByTitle(post1Title).get();
    }
}*/
