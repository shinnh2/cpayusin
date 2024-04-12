package com.jbaacount.payload.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.payload.response.QMemberDetailResponse is a Querydsl Projection type for MemberDetailResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDetailResponse extends ConstructorExpression<MemberDetailResponse> {

    private static final long serialVersionUID = 520596949L;

    public QMemberDetailResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> url, com.querydsl.core.types.Expression<Integer> score, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(MemberDetailResponse.class, new Class<?>[]{long.class, String.class, String.class, String.class, int.class, java.time.LocalDateTime.class}, id, nickname, email, url, score, createdAt);
    }

}

