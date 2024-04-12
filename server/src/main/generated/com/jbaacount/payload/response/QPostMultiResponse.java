package com.jbaacount.payload.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.payload.response.QPostMultiResponse is a Querydsl Projection type for PostMultiResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostMultiResponse extends ConstructorExpression<PostMultiResponse> {

    private static final long serialVersionUID = -1777945711L;

    public QPostMultiResponse(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> memberName, com.querydsl.core.types.Expression<Long> boardId, com.querydsl.core.types.Expression<String> boardName, com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> commentsCount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(PostMultiResponse.class, new Class<?>[]{long.class, String.class, long.class, String.class, long.class, String.class, String.class, int.class, java.time.LocalDateTime.class}, memberId, memberName, boardId, boardName, id, title, content, commentsCount, createdAt);
    }

}

