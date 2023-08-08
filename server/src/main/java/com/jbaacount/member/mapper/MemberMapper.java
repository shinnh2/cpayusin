package com.jbaacount.member.mapper;

import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.entity.Member;
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
        response.setProfileImage(member.getFile() != null ? member.getFile().getUrl() : null);
        response.setCreatedAt(member.getCreatedAt());
        response.setModifiedAt(member.getModifiedAt());

        return response;
    }

    public MemberInfoForResponse memberToMemberInfo(Member member)
    {
        MemberInfoForResponse response = new MemberInfoForResponse();
        response.setId(member.getId());
        response.setNickname(member.getNickname());

        return response;
    }
}
