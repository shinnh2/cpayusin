package com.jbaacount.vote.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.entity.Category;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
class VoteFacadeTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private VoteFacade voteFacade;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(VoteFacadeTest.class);

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    private static final String boardName = "첫번째 게시판";
    private static final String categoryName = "JPA란";
    private static final String post1Title = "게시판1";
    private static final String post2Title = "게시판2";


    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);
        Member user = TestUtil.createUserMember(memberService);

        logger.info("admin nickname = {}", admin.getNickname());

        Board board1 = Board.builder()
                .name(boardName)
                .isAdminOnly(false)
                .build();

        Board board2 = Board.builder()
                .name("게시판2")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board1, admin);
        boardService.createBoard(board2, admin);

        Category category = Category.builder()
                .name(categoryName)
                .isAdminOnly(false)
                .build();

        categoryService.createCategory(category, board1.getId(), admin);

        Post post1 = Post
                .builder()
                .title(post1Title)
                .content("내용 테스트용1")
                .build();

        Post post2 = Post
                .builder()
                .title(post2Title)
                .content("내용 테스트용2")
                .build();

        postService.createPost(post1, category.getId(), board1.getId(), user);
        postService.createPost(post2, null, board1.getId(), admin);

    }

    @Test
    void vote_multiThread() throws InterruptedException
    {
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Post post = getPost();
        Member admin = getAdmin();
        Member user = getUser();

        executorService.submit(() -> {
            logger.info("===thread start===");
            try{
                logger.info("into vote facade logic ");
                voteFacade.votePost(admin, post.getId());
            } catch (InterruptedException e)
            {
                System.out.println("exception happens");
                throw new RuntimeException(e);
            } finally
            {
                countDownLatch.countDown();
            }
        });

        executorService.submit(() -> {
            logger.info("===thread start===");
            try{
                logger.info("into vote facade logic ");
                voteFacade.votePost(user, post.getId());
            } catch (InterruptedException e)
            {
                System.out.println("exception happens");
                throw new RuntimeException(e);
            } finally
            {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

        assertThat(getPost().getVoteCount()).isEqualTo(2);
    }

    private Post getPost()
    {
        Post post = postRepository.findByTitle(post1Title).get();
        System.out.println("===getPost()===");
        System.out.println("post = " + post.getTitle());
        return post;
    }

    private Member getAdmin()
    {
        return memberRepository.findByEmail(adminEmail).get();
    }

    private Member getUser()
    {
        return memberRepository.findByEmail(userEmail).get();
    }
}
