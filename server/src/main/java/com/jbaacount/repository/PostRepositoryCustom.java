package com.jbaacount.repository;

import com.jbaacount.global.dto.PageDto;
import com.jbaacount.payload.response.PostMultiResponseDto;
import com.jbaacount.payload.response.PostResponse;
import com.jbaacount.payload.response.PostResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{
    //SliceDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Long last, Pageable pageable);

    //SliceDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Long last, Pageable pageable);


    //SliceDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Long last, Pageable pageable);

    PageDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Pageable pageable);

    PageDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Pageable pageable);

    PageDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Pageable pageable);


}
