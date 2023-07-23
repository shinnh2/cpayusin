package com.jbaacount.comment.mapper;

import com.jbaacount.comment.dto.request.CommentPostDto;
import com.jbaacount.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper
{
    public Comment postToComment(CommentPostDto request)
    {
        Comment comment = Comment.builder()
                .text(request.getText())
                .build();

        return comment;
    }


}
