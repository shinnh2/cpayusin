package com.jbaacount.payload.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.payload.response.QPostResponseForProfile is a Querydsl Projection type for PostResponseForProfile
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostResponseForProfile extends ConstructorExpression<PostResponseForProfile> {

    private static final long serialVersionUID = 1302378282L;

    public QPostResponseForProfile(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(PostResponseForProfile.class, new Class<?>[]{long.class, String.class, java.time.LocalDateTime.class}, id, title, createdAt);
    }

}

