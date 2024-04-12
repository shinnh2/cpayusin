package com.jbaacount.payload.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.payload.response.QCommentResponseForProfile is a Querydsl Projection type for CommentResponseForProfile
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentResponseForProfile extends ConstructorExpression<CommentResponseForProfile> {

    private static final long serialVersionUID = -1499365065L;

    public QCommentResponseForProfile(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> postId, com.querydsl.core.types.Expression<String> text, com.querydsl.core.types.Expression<Integer> voteCount, com.querydsl.core.types.Expression<Boolean> isRemoved, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(CommentResponseForProfile.class, new Class<?>[]{long.class, long.class, String.class, int.class, boolean.class, java.time.LocalDateTime.class}, id, postId, text, voteCount, isRemoved, createdAt);
    }

}

