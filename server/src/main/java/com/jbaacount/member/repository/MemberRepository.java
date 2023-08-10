package com.jbaacount.member.repository;

import com.jbaacount.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom
{
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where REPLACE(lower(m.nickname), ' ', '') = REPLACE(lower(:nickname), ' ', '')")
    Optional<Member> findByNickname(String nickname);

    @Query("select m.score from Member m where m.score > 0 order by m.score desc limit 3")
    List<Integer> find3rdScore();

    @Query("select m from Member m where m.score >= :score order by m.score desc")
    List<Member> findTop3MembersByScore(Integer score);
}
