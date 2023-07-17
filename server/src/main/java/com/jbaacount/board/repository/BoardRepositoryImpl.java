package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardInfoForResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.jbaacount.post.repository.PostRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.jbaacount.board.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PostRepository postRepository;

    @Override
    public BoardInfoForResponse getBoardResponseInfo(Long boardId, Pageable pageable)
    {
        BoardInfoForResponse boardResult = query
                .select(extractBoardInfo())
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();

        Page<PostInfoForResponse> postsInfo = postRepository.getAllPostsInfoForBoard(boardId, pageable);
        boardResult.setPosts(postsInfo);

        return boardResult;
    }

    ConstructorExpression<BoardInfoForResponse> extractBoardInfo()
    {
        return Projections.constructor(BoardInfoForResponse.class,
                board.id,
                board.name);
    }
}
