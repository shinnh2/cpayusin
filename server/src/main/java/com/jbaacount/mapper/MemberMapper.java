package com.jbaacount.mapper;

import com.jbaacount.domain.Member;
import com.jbaacount.dto.request.member.MemberPathDto;
import com.jbaacount.dto.request.member.MemberPostDto;
import com.jbaacount.dto.response.MemberResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper
{
    public Member postToMember(MemberPostDto postDto)
    {
        if(postDto == null)
            return null;

        String authority = "USER";

        Member member = Member.builder()
                .nickname(postDto.getNickname())
                .email(postDto.getEmail())
                .password(postDto.getPassword())
                .authority(authority)
                .build();

        return member;
    }

    public Member patchToMember(MemberPathDto patchDto)
    {
        if(patchDto == null)
            return null;

        Member member = Member.builder()
                .id(patchDto.getId())
                .nickname(patchDto.getNickname())
                .password(patchDto.getPassword())
                .build();

        return member;
    }

    public MemberResponseDto responseToMember(Member member)
    {
        MemberResponseDto response = MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .authority(member.getAuthority())
                .build();

        return response;
    }
}
