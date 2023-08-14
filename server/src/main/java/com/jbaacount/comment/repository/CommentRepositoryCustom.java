package com.jbaacount.comment.repository;

import com.jbaacount.comment.dto.response.CommentMultiResponse;
import com.jbaacount.comment.dto.response.CommentResponseForProfile;
import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom
{
    List<CommentMultiResponse> getAllComments(Long postId, Pageable pageable, Member currentMember);

    SliceDto<CommentResponseForProfile> getAllCommentsForProfile(Long memberId, Long last, Pageable pageable);
}
