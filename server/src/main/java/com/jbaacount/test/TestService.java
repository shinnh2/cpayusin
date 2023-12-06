/*
package com.jbaacount.test;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.comment.entity.Comment;
import com.jbaacount.comment.service.CommentService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.service.PostService;
import com.jbaacount.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TestService implements CommandLineRunner
{
    private final MemberService memberService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final VoteService voteService;

    @Transactional
    @Override
    public void run(String... args) throws Exception
    {
        Member admin = Member.builder().email("mike@ticonsys.com").nickname("운영자").password("123456789").build();
        Member member1 = Member.builder().email("member1@naver.com").nickname("회원1").password("123456789").build();
        Member member2 = Member.builder().email("member2@naver.com").nickname("회원2").password("123456789").build();
        Member member3 = Member.builder().email("member3@naver.com").nickname("회원3").password("123456789").build();

        memberService.createMember(admin);
        memberService.createMember(member1); // 15점 게시글 5개
        memberService.createMember(member2); // 12점 게시글 2개 + 투표 3개
        memberService.createMember(member3); // 12점 게시글 4개

        for(int i = 0; i < 20; i++)
        {
            memberService.createMember(Member.builder().email("aaaaa@naver.com"+i).nickname("회원테스트"+i).password("123456789").build());
        }

        Board board1 = Board.builder().name("첫번째 게시판").isAdminOnly(false).build();
        Board board2 = Board.builder().name("두번째 게시판").isAdminOnly(false).build();

        boardService.createBoard(board1, admin);
        boardService.createBoard(board2, admin);
        for(int i = 3; i < 11; i++)
        {
            boardService.createBoard(Board.builder().name(i + "번째 게시판").isAdminOnly(false).build(), admin);
        }

        Category category1 = categoryService.createCategory(Category.builder().name("카테고리a").isAdminOnly(false).build(), board1.getId(), admin);
        Category category2 = categoryService.createCategory(Category.builder().name("카테고리b").isAdminOnly(false).build(), board1.getId(), admin);
        Category category3 = categoryService.createCategory(Category.builder().name("카테고리c").isAdminOnly(false).build(), board2.getId(), admin);
        Category category4 = categoryService.createCategory(Category.builder().name("카테고리d").isAdminOnly(false).build(), board2.getId(), admin);


        for(int i = 0; i < 10; i++)
        {
            postService.createPost(Post.builder().title("게시글"+i).content("1234").build(), null, category1.getId(), board1.getId(), admin);
            postService.createPost(Post.builder().title("문제풀이"+i).content("abcde").build(), null, category2.getId(), board1.getId(), admin);
            postService.createPost(Post.builder().title("질문글"+i).content("가나다라").build(), null, category3.getId(), board2.getId(), admin);
            postService.createPost(Post.builder().title("핵심요약"+i).content("aeiou").build(), null, category4.getId(), board2.getId(), admin);

            categoryService.createCategory(Category.builder().name("카테고리"+i).isAdminOnly(false).build(), board1.getId(), admin);
        }


        Post adminPost = Post.builder().title("핵심요약").content("aeiou").build();
        postService.createPost(adminPost, null, category4.getId(), board2.getId(), admin);

        for(int i = 0; i < 4; i++)
        {
            //member1 게시물 등록
            postService.createPost(Post.builder().title("게시글 테스트").content("12345").build(), null, category1.getId(), board1.getId(), member1);

            //member3 게시물 등록
            postService.createPost(Post.builder().title("게시글 테스트2").content("12345").build(), null, category1.getId(), board1.getId(), member3);
        }
        postService.createPost(Post.builder().title("게시글 테스트").content("12345").build(), null, category1.getId(), board1.getId(), member1);

        //member2의 게시물 등록
        postService.createPost(Post.builder().title("게시글 테스트3").content("12345").build(), null, category1.getId(), board1.getId(), member2);
        postService.createPost(Post.builder().title("게시글 테스트3").content("12345").build(), null, category1.getId(), board1.getId(), member2);

        //member2의 게시물 투표
        voteService.votePost(admin, member2.getPosts().get(0));
        voteService.votePost(admin, member2.getPosts().get(1));
        voteService.votePost(member1, member2.getPosts().get(0));

        for(int i = 0; i < 10; i++)
        {
            commentService.saveComment(Comment.builder().text("댓글 테스트").build(), adminPost.getId(), null, admin);
        }

    }
}
*/
