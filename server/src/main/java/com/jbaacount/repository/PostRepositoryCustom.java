package com.jbaacount.repository;

import com.jbaacount.payload.response.PostMultiResponse;
import com.jbaacount.payload.response.PostResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{

    Page<PostResponseForProfile> getPostsByMemberId(Long memberId, Pageable pageable);

    Page<PostMultiResponse> getPostsByBoardId(Long boardId, String keyword, Pageable pageable);

    Page<PostMultiResponse> getPostsByCategoryId(Long categoryId, String keyword, Pageable pageable);
}
