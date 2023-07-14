package com.jbaacount.member.repository;

import com.jbaacount.global.utils.PaginationUtils;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.jbaacount.member.entity.QMember.member;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PaginationUtils paginationUtils;

    @Override
    public Slice<MemberResponseDto> findAllMembers(String keyword, Long memberId, Pageable pageable)
    {
        log.info("===findAllMembers in repository===");
        List<MemberResponseDto> memberDto = query
                .select(memberToResponse())
                .from(member)
                .where(ltMemberId(memberId))
                .where(checkEmailKeyword(keyword), (checkNicknameKeyword(keyword)))
                .orderBy(member.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        log.info("===memberDto===", memberDto.size());

        return paginationUtils.toSlice(pageable, memberDto);
    }

    public ConstructorExpression<MemberResponseDto> memberToResponse()
    {
        log.info("===memberToResponse===");
        return Projections.constructor(MemberResponseDto.class,
                member.id,
                member.email,
                member.password,
                member.nickname,
                member.createdAt,
                member.modifiedAt);
    }

    private BooleanExpression ltMemberId(Long memberId)
    {
        return memberId != null ? member.id.lt(memberId) : null;
    }


    private BooleanExpression checkEmailKeyword(String keyword)
    {
         return keyword != null ? member.email.lower().contains(keyword.toLowerCase()) : null;
    }

    private BooleanExpression checkNicknameKeyword(String keyword)
    {
        return keyword != null ? member.nickname.lower().contains(keyword.toLowerCase()) : null;
    }

}
