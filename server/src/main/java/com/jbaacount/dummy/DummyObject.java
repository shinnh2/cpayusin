package com.jbaacount.dummy;

import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
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
                .password(getEncodedPassword())
                .build();
    }

    protected Member newMockMember(Long id, String email, String nickname, String role)
    {
        Member member = Member.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .password(getEncodedPassword())
                .build();

        member.setCreatedAt(LocalDateTime.now());
        member.setModifiedAt(LocalDateTime.now());
        member.setRoles(List.of(role));

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


    private String getEncodedPassword()
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode("12345");
    }
}
