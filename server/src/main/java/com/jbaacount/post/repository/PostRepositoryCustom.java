package com.jbaacount.post.repository;

import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.post.dto.response.PostMultiResponseDto;
import com.jbaacount.post.dto.response.PostResponseForProfile;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{
    SliceDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Long last, Pageable pageable);

    SliceDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Long last, Pageable pageable);

    SliceDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Long last, Pageable pageable);
}
