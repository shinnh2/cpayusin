package com.jbaacount.board.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jbaacount.board.dto.response.QBoardAndCategoryResponse is a Querydsl Projection type for BoardAndCategoryResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardAndCategoryResponse extends ConstructorExpression<BoardAndCategoryResponse> {

    private static final long serialVersionUID = 1392667394L;

    public QBoardAndCategoryResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Long> orderIndex) {
        super(BoardAndCategoryResponse.class, new Class<?>[]{long.class, String.class, long.class}, id, name, orderIndex);
    }

}

