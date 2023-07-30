package com.jbaacount.comment.mapper;

import com.jbaacount.comment.dto.request.CommentPostDto;
import com.jbaacount.comment.dto.response.CommentSingleResponse;
import com.jbaacount.comment.entity.Comment;
import com.jbaacount.member.dto.response.MemberInfoResponseDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.vote.entity.Vote;
import com.jbaacount.vote.repository.VoteRepository;
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

        MemberInfoResponseDto memberResponse = memberMapper.memberToInfoResponse(entity.getMember());
        boolean voteStatus = false;

        if(currentMember != null)
        {
            Optional<Vote> optionalVote = voteRepository.checkMemberVotedCommentOrNot(currentMember, entity);
            voteStatus = optionalVote.isPresent();
        }

        CommentSingleResponse response = CommentSingleResponse.builder()
                .id(entity.getId())
                .parentId(parentId)
                .text(entity.getText())
                .voteStatus(voteStatus)
                .member(memberResponse)
                .build();

        return response;
    }
}
