package com.jbaacount.repository;

import com.jbaacount.payload.response.CommentResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom
{
    Page<CommentResponseForProfile> getAllCommentsForProfile(Long memberId, Pageable pageable);
}
