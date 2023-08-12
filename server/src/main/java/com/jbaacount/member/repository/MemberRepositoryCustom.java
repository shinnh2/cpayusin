package com.jbaacount.member.repository;

import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom
{
    SliceDto<MemberResponseDto> findAllMembers(String keyword, Long memberId, Pageable pageable);

}
