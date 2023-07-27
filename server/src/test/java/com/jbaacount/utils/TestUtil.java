package com.jbaacount.utils;

import com.jbaacount.board.service.BoardService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.entity.Post;

public class TestUtil
{
    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    public static Member createAdminMember(MemberService memberService)
    {
        Member admin = Member.builder()
                .nickname("관리자")
                .email(adminEmail)
                .password("123123")
                .build();
        return memberService.createMember(admin);
    }

    public static Member createUserMember(MemberService memberService)
    {
        Member user = Member.builder()
                .nickname("유저")
                .email(userEmail)
                .password("123123")
                .build();

        return memberService.createMember(user);
    }
}
