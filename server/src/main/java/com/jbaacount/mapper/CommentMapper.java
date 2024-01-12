package com.jbaacount.mapper;

import com.jbaacount.model.Comment;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.response.CommentSingleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper
{
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    Comment toCommentEntity(CommentCreateRequest request);

    default CommentSingleResponse toCommentSingleResponse(Comment entity, boolean voteStatus)
    {
        Long parentId = null;
        if(entity.getParent() != null)
            parentId = entity.getParent().getId();

        return CommentSingleResponse.builder()
                .memberId(entity.getMember().getId())
                .nickname(entity.getMember().getNickname())
                .commentId(entity.getId())
                .parentId(parentId)
                .text(entity.getText())
                .voteCount(entity.getVoteCount())
                .voteStatus(voteStatus)
                .isRemoved(entity.isRemoved())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
