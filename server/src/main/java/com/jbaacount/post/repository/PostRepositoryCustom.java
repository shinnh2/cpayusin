package com.jbaacount.post.repository;

import com.jbaacount.post.dto.response.PostInfoForCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom
{
    Page<PostInfoForCategory> getAllPostsForCategory(Long categoryId, Pageable pageable);
}
