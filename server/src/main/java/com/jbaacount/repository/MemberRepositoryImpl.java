package com.jbaacount.repository;

import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.global.utils.PaginationUtils;
import com.jbaacount.payload.response.member.MemberMultiResponse;
import com.jbaacount.payload.response.member.MemberScoreResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

import static com.jbaacount.model.QFile.file;
import static com.jbaacount.model.QMember.member;
import static com.jbaacount.model.QPost.post;
import static com.jbaacount.model.QVote.vote;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PaginationUtils paginationUtils;

    @Override
    public SliceDto<MemberMultiResponse> findAllMembers(String keyword, Long memberId, Pageable pageable)
    {
        log.info("===findAllMembers in repository===");
        List<MemberMultiResponse> memberDto = query
                .select(getMemberList())
                .from(member)
                .leftJoin(member.file, file)
                .where(ltMemberId(memberId))
                .where(checkEmailKeyword(keyword))
                .where(checkNicknameKeyword(keyword))
                .orderBy(member.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        log.info("list size = {}", memberDto.size());

        Slice<MemberMultiResponse> slice = paginationUtils.toSlice(pageable, memberDto);

        return new SliceDto<>(memberDto, slice);
    }

    @Override
    public List<MemberScoreResponse> memberResponseForReward(LocalDateTime startMonth, LocalDateTime endMonth)
    {
        return query
                .select(extractMemberScoreResponse())
                .from(member)
                .leftJoin(member.posts, post)
                .leftJoin(post.votes, vote)
                .where(post.createdAt.between(startMonth, endMonth))
                .where(post.member.email.ne("mike@ticonsys.com"))
                .groupBy(member.id)
                .orderBy(
                        member.score.desc(), //점수 기준
                        post.count().desc(), //해당 월에 작성한 게시글 기준
                        post.voteCount.sum().desc(), //해당 월에 받은 투표 개수 기준
                        member.posts.size().desc(), //그 동안의 총 개시물 갯수
                        member.createdAt.asc() //가입 날짜 오래 된 순
                )
                .limit(3)
                .fetch();

        /*List<MemberScoreResponse> responses = memberListTuple.stream()
                .map(tuple -> new MemberScoreResponse(tuple.get(member.id), tuple.get(member.nickname), tuple.get(member.score)))
                .collect(Collectors.toList());

        return responses;*/
    }

    private ConstructorExpression<MemberMultiResponse> getMemberList()
    {
        log.info("===memberToResponse===");
        return Projections.constructor(MemberMultiResponse.class,
                member.id,
                member.nickname,
                member.email,
                member.file != null ? member.file.url : null,
                member.score,
                member.role);
    }

    private ConstructorExpression<MemberScoreResponse> extractMemberScoreResponse()
    {
        return Projections.constructor(MemberScoreResponse.class,
                member.id,
                member.nickname,
                member.score);
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
