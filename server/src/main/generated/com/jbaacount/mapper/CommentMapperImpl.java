package com.jbaacount.mapper;

import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.response.CommentChildrenResponse;
import com.jbaacount.payload.response.CommentParentResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T17:54:48+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment toCommentEntity(CommentCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.text( request.getText() );

        return comment.build();
    }

    @Override
    public CommentChildrenResponse toCommentChildrenResponse(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentChildrenResponse commentChildrenResponse = new CommentChildrenResponse();

        commentChildrenResponse.setMemberId( commentMemberId( comment ) );
        commentChildrenResponse.setMemberName( commentMemberNickname( comment ) );
        commentChildrenResponse.setParentId( commentParentId( comment ) );
        commentChildrenResponse.setId( comment.getId() );
        commentChildrenResponse.setText( comment.getText() );
        commentChildrenResponse.setIsRemoved( comment.getIsRemoved() );
        commentChildrenResponse.setCreatedAt( comment.getCreatedAt() );

        commentChildrenResponse.setVoteCount( comment.getVotes().size() );

        return commentChildrenResponse;
    }

    @Override
    public List<CommentChildrenResponse> toCommentChildrenResponseList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentChildrenResponse> list = new ArrayList<CommentChildrenResponse>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toCommentChildrenResponse( comment ) );
        }

        return list;
    }

    @Override
    public CommentParentResponse toCommentParentResponse(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentParentResponse commentParentResponse = new CommentParentResponse();

        commentParentResponse.setMemberId( commentMemberId( comment ) );
        commentParentResponse.setMemberName( commentMemberNickname( comment ) );
        commentParentResponse.setId( comment.getId() );
        commentParentResponse.setText( comment.getText() );
        commentParentResponse.setIsRemoved( comment.getIsRemoved() );
        commentParentResponse.setCreatedAt( comment.getCreatedAt() );
        commentParentResponse.setChildren( toCommentChildrenResponseList( comment.getChildren() ) );

        commentParentResponse.setVoteCount( comment.getVotes().size() );

        return commentParentResponse;
    }

    @Override
    public List<CommentParentResponse> toCommentParentResponseList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentParentResponse> list = new ArrayList<CommentParentResponse>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toCommentParentResponse( comment ) );
        }

        return list;
    }

    private Long commentMemberId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Member member = comment.getMember();
        if ( member == null ) {
            return null;
        }
        Long id = member.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String commentMemberNickname(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Member member = comment.getMember();
        if ( member == null ) {
            return null;
        }
        String nickname = member.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    private Long commentParentId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Comment parent = comment.getParent();
        if ( parent == null ) {
            return null;
        }
        Long id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
