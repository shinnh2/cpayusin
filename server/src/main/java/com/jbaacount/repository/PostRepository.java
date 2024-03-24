package com.jbaacount.repository;

import com.jbaacount.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom
{
    @Query(value = "select * from post p where p.title like %:keyword%", nativeQuery = true)
    Optional<Post> findByTitle(@Param("keyword") String keyword);


    @Query("SELECT p FROM Post p " +
            "WHERE p.board.id = :boardId " +
            "AND (:keyword IS NOT NULL OR p.board.name LIKE %:keyword%) " +
            "ORDER BY p.board.createdAt DESC")
    Page<Post> findAllByBoardId(@Param("boardId") Long boardId, @Param("keyword") String keyword, Pageable pageable);
}
