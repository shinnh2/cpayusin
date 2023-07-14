package com.jbaacount.member.repository;

import com.jbaacount.member.dto.response.MemberResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom
{
    Slice<MemberResponseDto> findAllMembers(String keyword, Long memberId, Pageable pageable);
}
