package com.jbaacount.mapper;

import com.jbaacount.model.Comment;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.response.CommentChildrenResponse;
import com.jbaacount.payload.response.CommentParentResponse;
import com.jbaacount.payload.response.CommentSingleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


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
                .voteCount(entity.getVotes().size())
                .voteStatus(voteStatus)
                .isRemoved(entity.getIsRemoved())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", source = "member.nickname")
    @Mapping(target = "voteCount", expression = "java(comment.getVotes().size())")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "voteStatus", ignore = true)
    CommentChildrenResponse toCommentChildrenResponse(Comment comment);

    List<CommentChildrenResponse> toCommentChildrenResponseList(List<Comment> comments);

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", source = "member.nickname")
    @Mapping(target = "voteCount", expression = "java(comment.getVotes().size())")
    @Mapping(target = "voteStatus", ignore = true)
    CommentParentResponse toCommentParentResponse(Comment comment);

    List<CommentParentResponse> toCommentParentResponseList(List<Comment> comments);
}
