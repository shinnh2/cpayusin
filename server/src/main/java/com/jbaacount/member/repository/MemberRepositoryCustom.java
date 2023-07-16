package com.jbaacount.member.repository;

import com.jbaacount.member.dto.response.MemberInfoResponseDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom
{
    Slice<MemberResponseDto> findAllMembers(String keyword, Long memberId, Pageable pageable);

}
