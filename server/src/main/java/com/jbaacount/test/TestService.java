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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestService implements CommandLineRunner
{
    private final MemberService memberService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Override
    public void run(String... args) throws Exception
    {
        Member admin = Member.builder().email("mike@ticonsys.com").nickname("운영자").password("123456789").build();
        memberService.createMember(admin);
        for(int i = 0; i < 20; i++)
        {
            memberService.createMember(Member.builder().email("aaaaa@naver.com"+i).nickname("회원테스트"+i).password("123456789").build());
        }

        Board board1 = Board.builder().name("첫번째 게시판").isAdminOnly(false).build();
        Board board2 = Board.builder().name("두번째 게시판").isAdminOnly(false).build();
        boardService.createBoard(board1, admin);
        boardService.createBoard(board2, admin);


        Category category1 = Category.builder().name("카테고리1").isAdminOnly(false).build();
        Category category2 = Category.builder().name("카테고리2").isAdminOnly(false).build();
        Category category3 = Category.builder().name("카테고리3").isAdminOnly(false).build();
        Category category4 = Category.builder().name("카테고리4").isAdminOnly(false).build();


        categoryService.createCategory(category1, board1.getId(), admin);
        categoryService.createCategory(category2, board1.getId(), admin);
        categoryService.createCategory(category3, board2.getId(), admin);
        categoryService.createCategory(category4, board2.getId(), admin);

        for(int i = 0; i < 10; i++)
        {
            postService.createPost(Post.builder().title("게시글"+i).content("1234").build(), null, category1.getId(), board1.getId(), admin);
            postService.createPost(Post.builder().title("문제풀이"+i).content("abcde").build(), null, category2.getId(), board1.getId(), admin);
            postService.createPost(Post.builder().title("질문글"+i).content("가나다라").build(), null, category3.getId(), board2.getId(), admin);
            postService.createPost(Post.builder().title("핵심요약"+i).content("aeiou").build(), null, category4.getId(), board2.getId(), admin);
        }

        Post adminPost = Post.builder().title("핵심요약").content("aeiou").build();
        postService.createPost(adminPost, null, category4.getId(), board2.getId(), admin);

        for(int i = 0; i < 10; i++)
        {
            commentService.saveComment(Comment.builder().text("댓글 테스트").build(), adminPost.getId(), null, admin);
        }
    }
}
