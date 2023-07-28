package com.jbaacount.member.mapper;

import com.jbaacount.member.dto.response.MemberInfoResponseDto;
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
        MemberResponseDto response = new MemberResponseDto();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setEmail(member.getEmail());
        response.setRoles(member.getRoles());
        response.setCreatedAt(member.getCreatedAt());
        response.setModifiedAt(member.getModifiedAt());

        return response;
    }

    public MemberInfoResponseDto memberToInfoResponse(Member member)
    {
        MemberInfoResponseDto response = MemberInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .build();

        return response;
    }
}
