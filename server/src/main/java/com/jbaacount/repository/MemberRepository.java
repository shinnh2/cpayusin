package com.jbaacount.repository;

import com.jbaacount.model.Member;
import com.jbaacount.payload.response.member.MemberSingleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom
{
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("select m from Member m where REPLACE(lower(m.nickname), ' ', '') = REPLACE(lower(:nickname), ' ', '')")
    Optional<Member> findByNickname(@Param("nickname") String nickname);

    boolean existsByEmail(@Param("email") String email);

    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("SELECT new com.jbaacount.payload.response.member.MemberSingleResponse(m.id, m.nickname, m.url, m.role) FROM Member m WHERE m.id = :memberId ")
    Optional<MemberSingleResponse> findSingleResponseById(@Param("memberId") Long memberId);
}
