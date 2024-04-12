package com.jbaacount.payload.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.payload.response.QMemberSimpleResponse is a Querydsl Projection type for MemberSimpleResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberSimpleResponse extends ConstructorExpression<MemberSimpleResponse> {

    private static final long serialVersionUID = 1738200630L;

    public QMemberSimpleResponse(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> memberNickName) {
        super(MemberSimpleResponse.class, new Class<?>[]{long.class, String.class}, memberId, memberNickName);
    }

}

