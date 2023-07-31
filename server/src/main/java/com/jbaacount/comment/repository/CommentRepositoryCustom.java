package com.jbaacount.comment.repository;

import com.jbaacount.comment.dto.response.CommentMultiResponse;
import com.jbaacount.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom
{
    Page<CommentMultiResponse> getAllComments(Long postId, Pageable pageable, Member currentMember);
}
