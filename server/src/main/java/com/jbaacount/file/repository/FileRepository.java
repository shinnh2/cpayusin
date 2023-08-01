package com.jbaacount.file.repository;

import com.jbaacount.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long>
{
    List<File> findByPostId(Long postId);
}
