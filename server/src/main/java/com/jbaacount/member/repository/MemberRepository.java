package com.jbaacount.member.repository;

import com.jbaacount.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom
{
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
