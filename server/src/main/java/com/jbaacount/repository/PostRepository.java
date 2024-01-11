package com.jbaacount.repository;

import com.jbaacount.model.Post;
import com.jbaacount.payload.response.PostResponse;
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

    @Query(nativeQuery = true, value = "SELECT * FROM POST")
    Page<PostResponse> getPostsByBoardId(Long boardId, String keyword, Pageable pageable);

    Page<PostResponse> getPostsByCategoryId(Long categoryId, String keyword, Pageable pageable);
}
