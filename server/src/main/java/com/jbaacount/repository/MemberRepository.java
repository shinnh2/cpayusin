package com.jbaacount.repository;

import com.jbaacount.model.Member;
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
}
