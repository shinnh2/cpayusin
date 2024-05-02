package com.jbaacount.dummy;

import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.model.type.Platform;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class DummyObject
{
    protected Member newMember(String email, String nickname)
    {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .platform(Platform.HOME)
                .password(getEncodedPassword())
                .build();
    }

    protected Member newMockMember(Long id, String email, String nickname, String role)
    {
        Member member = Member.builder()
                .id(id)
                .email(email)
                .platform(Platform.HOME)
                .nickname(nickname)
                .password(getEncodedPassword())
                .role(role)
                .build();

        member.setCreatedAt(LocalDateTime.now());
        member.setModifiedAt(LocalDateTime.now());

        return member;
    }

    protected Board newBoard(String name, int orderIndex)
    {
        Board board = Board.builder()
                .isAdminOnly(false)
                .name(name)
                .build();

        board.setOrderIndex(orderIndex);
        board.setCreatedAt(LocalDateTime.now());
        board.setModifiedAt(LocalDateTime.now());

        return board;
    }

    protected Board newMockBoard(Long id, String name, int orderIndex)
    {
        Board board = Board.builder()
                .isAdminOnly(false)
                .name(name)
                .build();

        board.setId(id);
        board.setOrderIndex(orderIndex);
        board.setCreatedAt(LocalDateTime.now());
        board.setModifiedAt(LocalDateTime.now());

        return board;
    }

    protected Post newPost(String title, String content, Board board, Member member)
    {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();

        post.addBoard(board);
        post.addMember(member);

        return post;
    }

    protected Post newMockPost(Long id, String title, String content, Board board, Member member)
    {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();
        post.setId(id);
        post.addBoard(board);
        post.addMember(member);

        return post;
    }


    private String getEncodedPassword()
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode("123456789");
    }
}
