package com.jbaacount.repository;

import com.jbaacount.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom
{
    List<Comment> findAllByPostId(@Param("postId") Long postId);

}
