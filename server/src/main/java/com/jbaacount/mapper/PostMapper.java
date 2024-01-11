package com.jbaacount.mapper;

import com.jbaacount.payload.response.FileResponseDto;
import com.jbaacount.model.File;
import com.jbaacount.payload.response.MemberInfoForResponse;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.PostPostDto;
import com.jbaacount.payload.response.PostSingleResponseDto;
import com.jbaacount.model.Post;
import com.jbaacount.model.Vote;
import com.jbaacount.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PostMapper
{
    private final MemberMapper memberMapper;
    private final VoteRepository voteRepository;

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

    public PostSingleResponseDto postEntityToResponse(Post entity, Member currentMember)
    {
        MemberInfoForResponse memberResponse = memberMapper.memberToMemberInfo(entity.getMember());
        boolean voteStatus = false;

        if(currentMember != null)
        {
            Optional<Vote> vote = voteRepository.checkMemberVotedPostOrNot(currentMember, entity);
            voteStatus = vote.isPresent();
        }

        List<File> files = entity.getFiles();
        List<FileResponseDto> fileResponses = new ArrayList<>();
        for (File file : files)
        {
            fileResponses.add(new FileResponseDto(file));
        }

        PostSingleResponseDto response = PostSingleResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .files(fileResponses)
                .voteCount(entity.getVoteCount())
                .voteStatus(voteStatus)
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .member(memberResponse)
                .build();

        return response;
    }

}
