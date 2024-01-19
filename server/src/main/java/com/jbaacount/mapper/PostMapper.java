package com.jbaacount.mapper;

import com.jbaacount.model.File;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.request.PostUpdateRequest;
import com.jbaacount.payload.response.FileResponse;
import com.jbaacount.payload.response.PostMultiResponse;
import com.jbaacount.payload.response.PostSingleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.jbaacount.service.UtilService.calculateTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper
{
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    Post toPostEntity(PostCreateRequest request);

    default void updatePostFromUpdateRequest(PostUpdateRequest postUpdateRequest, Post post)
    {
        if ( postUpdateRequest == null ) {
            return;
        }

        if ( postUpdateRequest.getTitle() != null ) {
            post.updateTitle( postUpdateRequest.getTitle() );
        }
        if ( postUpdateRequest.getContent() != null ) {
            post.updateContent( postUpdateRequest.getContent() );
        }
    }

    default PostSingleResponse toPostSingleResponse(Post entity, boolean voteStatus)
    {
        if( entity == null) {
            return null;
        }

        Long categoryId = null;
        if(entity.getCategory() != null)
            categoryId = entity.getCategory().getId();

        return PostSingleResponse.builder()
                .memberId(entity.getMember().getId())
                .boardId(entity.getBoard().getId())
                .categoryId(categoryId)
                .nickname(entity.getMember().getNickname())
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .files(mapFiles(entity.getFiles()))
                .voteCount(entity.getVoteCount())
                .voteStatus(voteStatus)
                .createdAt(calculateTime(entity.getCreatedAt()))
                .build();
    }

    default List<FileResponse> mapFiles(List<File> files)
    {
        if(files == null)
            return null;

        return FileMapper.INSTANCE.toFileResponseList(files);
    }


    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", source = "member.nickname")
    @Mapping(target = "boardId", source = "board.id")
    @Mapping(target = "boardName", source = "board.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    PostMultiResponse toPostMultiResponse(Post post);

}
