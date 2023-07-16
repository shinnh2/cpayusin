package com.jbaacount.post.mapper;

import com.jbaacount.member.dto.response.MemberInfoResponseDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.post.dto.request.PostPostDto;
import com.jbaacount.post.dto.response.PostResponseDto;
import com.jbaacount.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostMapper
{
    private final MemberMapper memberMapper;

    public Post postDtoToPostEntity(PostPostDto request)
    {
        if(request == null)
            return null;

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return post;
    }

    public PostResponseDto postEntityToResponse(Post entity)
    {
        MemberInfoResponseDto memberResponse = memberMapper.memberToInfoResponse(entity.getMember());

        PostResponseDto response = PostResponseDto.builder()
                .boardId(entity.getBoard().getId())
                .boardName(entity.getBoard().getName())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .member(memberResponse)
                .build();

        return response;
    }

    public List<PostResponseDto> postEntityToListResponse(List<Post> entities)
    {
        return entities.stream()
                .map(entity -> postEntityToResponse(entity))
                .collect(Collectors.toList());
    }
}
