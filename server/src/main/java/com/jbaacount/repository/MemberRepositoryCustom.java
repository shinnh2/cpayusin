package com.jbaacount.repository;

import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.payload.response.MemberResponseDto;
import com.jbaacount.payload.response.MemberRewardResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepositoryCustom
{
    SliceDto<MemberResponseDto> findAllMembers(String keyword, Long memberId, Pageable pageable);

    List<MemberRewardResponse> memberResponseForReward(LocalDateTime now);

}
