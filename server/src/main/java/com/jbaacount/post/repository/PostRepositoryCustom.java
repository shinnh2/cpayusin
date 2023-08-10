package com.jbaacount.post.repository;

import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import com.jbaacount.post.dto.response.PostResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom
{
    Page<PostInfoForOtherResponse> getAllPostsInfoForCategory(Long categoryId, Pageable pageable);

    Page<PostInfoForOtherResponse> getAllPostsInfoForBoard(Long boardId, Pageable pageable);

    Slice<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Long last, Pageable pageable);
}
