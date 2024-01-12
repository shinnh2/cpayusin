package com.jbaacount.mapper;

import com.jbaacount.model.File;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.PostPostDto;
import com.jbaacount.payload.response.FileResponse;
import com.jbaacount.payload.response.PostSingleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper
{
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    Post toPostEntity(PostPostDto request);

    default PostSingleResponse toPostSingleResponse(Post entity, boolean voteStatus)
    {
        return PostSingleResponse.builder()
                .memberId(entity.getMember().getId())
                .nickname(entity.getMember().getNickname())
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .files(mapFiles(entity.getFiles()))
                .voteCount(entity.getVoteCount())
                .voteStatus(voteStatus)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    default List<FileResponse> mapFiles(List<File> files)
    {
        if(files == null)
            return null;

        return FileMapper.INSTANCE.toFileResponseList(files);
    }
}
