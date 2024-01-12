package com.jbaacount.repository;

import com.jbaacount.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long>
{
    List<File> findByPostId(Long postId);

    Optional<File> findByMemberId(Long memberId);
}
