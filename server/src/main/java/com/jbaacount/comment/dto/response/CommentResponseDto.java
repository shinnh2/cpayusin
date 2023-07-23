package com.jbaacount.comment.dto.response;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CommentResponseDto
{
    private Long id;
    private String text;
    private MemberInfoForResponse member;
    private List<CommentResponseDto> replies;

    @QueryProjection

    public CommentResponseDto(Long id, String text, MemberInfoForResponse member, List<CommentResponseDto> replies)
    {
        this.id = id;
        this.text = text;
        this.member = member;
        this.replies = replies;
    }
}
