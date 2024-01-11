package com.jbaacount.mapper;

import com.jbaacount.payload.request.CommentPostDto;
import com.jbaacount.payload.response.CommentSingleResponse;
import com.jbaacount.model.Comment;
import com.jbaacount.payload.response.MemberInfoForResponse;
import com.jbaacount.model.Member;
import com.jbaacount.model.Vote;
import com.jbaacount.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentMapper
{
    private final MemberMapper memberMapper;
    private final VoteRepository voteRepository;
    public Comment postToComment(CommentPostDto request)
    {
        Comment comment = Comment.builder()
                .text(request.getText())
                .build();

        return comment;
    }


    public CommentSingleResponse commentToResponse(Comment entity, Member currentMember)
    {
        Long parentId = null;
        if(entity.getParent() != null)
        {
            parentId = entity.getParent().getId();
        }

        MemberInfoForResponse memberResponse = memberMapper.memberToMemberInfo(entity.getMember());

        boolean voteStatus = false;

        if(currentMember != null)
        {
            Optional<Vote> optionalVote = voteRepository.checkMemberVotedCommentOrNot(currentMember.getId(), entity.getId());
            voteStatus = optionalVote.isPresent();
        }

        CommentSingleResponse response = CommentSingleResponse.builder()
                .id(entity.getId())
                .parentId(parentId)
                .text(entity.getText())
                .voteCount(entity.getVoteCount())
                .voteStatus(voteStatus)
                .createdAt(entity.getCreatedAt())
                .member(memberResponse)
                .build();

        return response;
    }
}
