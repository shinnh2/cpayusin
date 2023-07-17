package com.jbaacount.post.repository;

import com.jbaacount.post.dto.response.PostInfoForResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{
    Page<PostInfoForResponse> getAllPostsInfoForCategory(Long categoryId, Pageable pageable);

    Page<PostInfoForResponse> getAllPostsInfoForBoard(Long boardId, Pageable pageable);
}
