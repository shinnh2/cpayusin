package com.jbaacount.member.mapper;

import com.jbaacount.member.entity.Member;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper
{
    public Member postToMember(MemberPostDto postDto)
    {
        if(postDto == null)
            return null;

        Member member = Member.builder()
                .nickname(postDto.getNickname())
                .email(postDto.getEmail())
                .password(postDto.getPassword())
                .build();

        return member;
    }

    public MemberResponseDto memberToResponse(Member member)
    {
        MemberResponseDto response = MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRoles())
                .build();

        return response;
    }
}