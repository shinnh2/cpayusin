package com.jbaacount.repository;

import com.jbaacount.payload.response.CommentMultiResponse;
import com.jbaacount.payload.response.CommentResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom
{
    List<CommentMultiResponse> getAllComments(Long postId);


    Page<CommentResponseForProfile> getAllCommentsForProfile(Long memberId, Pageable pageable);
}
