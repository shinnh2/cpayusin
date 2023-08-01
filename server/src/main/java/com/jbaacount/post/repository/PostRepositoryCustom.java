package com.jbaacount.post.repository;

import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{
    Page<PostInfoForOtherResponse> getAllPostsInfoForCategory(Long categoryId, Pageable pageable);

    Page<PostInfoForOtherResponse> getAllPostsInfoForBoard(Long boardId, Pageable pageable);
}
