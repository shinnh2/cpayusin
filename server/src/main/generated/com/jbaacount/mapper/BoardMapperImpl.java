package com.jbaacount.mapper;

import com.jbaacount.model.Board;
import com.jbaacount.payload.request.board.BoardCreateRequest;
import com.jbaacount.payload.request.board.BoardUpdateRequest;
import com.jbaacount.payload.request.board.CategoryUpdateRequest;
import com.jbaacount.payload.response.board.BoardChildrenResponse;
import com.jbaacount.payload.response.board.BoardCreateResponse;
import com.jbaacount.payload.response.board.BoardMenuResponse;
import com.jbaacount.payload.response.board.BoardResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-09T17:45:27+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 17.0.7 (Azul Systems, Inc.)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public Board toBoardEntity(BoardCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Board.BoardBuilder board = Board.builder();

        board.name( request.getName() );
        board.isAdminOnly( request.getIsAdminOnly() );

        return board.build();
    }

    @Override
    public BoardCreateResponse toBoardCreateResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardCreateResponse boardCreateResponse = new BoardCreateResponse();

        boardCreateResponse.setId( board.getId() );
        boardCreateResponse.setName( board.getName() );
        boardCreateResponse.setOrderIndex( board.getOrderIndex() );
        boardCreateResponse.setIsAdminOnly( board.getIsAdminOnly() );

        return boardCreateResponse;
    }

    @Override
    public void updateBoard(BoardUpdateRequest request, Board board) {
        if ( request == null ) {
            return;
        }

        if ( request.getId() != null ) {
            board.setId( request.getId() );
        }
        if ( request.getName() != null ) {
            board.setName( request.getName() );
        }
        if ( request.getIsAdminOnly() != null ) {
            board.setIsAdminOnly( request.getIsAdminOnly() );
        }
        if ( request.getOrderIndex() != null ) {
            board.setOrderIndex( request.getOrderIndex() );
        }
    }

    @Override
    public void updateBoard(CategoryUpdateRequest request, Board board) {
        if ( request == null ) {
            return;
        }

        if ( request.getId() != null ) {
            board.setId( request.getId() );
        }
        if ( request.getName() != null ) {
            board.setName( request.getName() );
        }
        if ( request.getIsAdminOnly() != null ) {
            board.setIsAdminOnly( request.getIsAdminOnly() );
        }
        if ( request.getOrderIndex() != null ) {
            board.setOrderIndex( request.getOrderIndex() );
        }
    }

    @Override
    public BoardResponse boardToResponse(Board entity) {
        if ( entity == null ) {
            return null;
        }

        BoardResponse.BoardResponseBuilder boardResponse = BoardResponse.builder();

        boardResponse.parentId( entityParentId( entity ) );
        boardResponse.id( entity.getId() );
        boardResponse.name( entity.getName() );
        boardResponse.orderIndex( entity.getOrderIndex() );
        boardResponse.isAdminOnly( entity.getIsAdminOnly() );

        return boardResponse.build();
    }

    @Override
    public List<BoardResponse> toBoardResponseList(List<Board> boards) {
        if ( boards == null ) {
            return null;
        }

        List<BoardResponse> list = new ArrayList<BoardResponse>( boards.size() );
        for ( Board board : boards ) {
            list.add( boardToResponse( board ) );
        }

        return list;
    }

    @Override
    public List<BoardMenuResponse> toBoardMenuResponse(List<Board> boards) {
        if ( boards == null ) {
            return null;
        }

        List<BoardMenuResponse> list = new ArrayList<BoardMenuResponse>( boards.size() );
        for ( Board board : boards ) {
            list.add( toMenuResponse( board ) );
        }

        return list;
    }

    @Override
    public BoardMenuResponse toMenuResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardMenuResponse.BoardMenuResponseBuilder boardMenuResponse = BoardMenuResponse.builder();

        boardMenuResponse.id( board.getId() );
        boardMenuResponse.name( board.getName() );
        boardMenuResponse.type( board.getType() );
        boardMenuResponse.orderIndex( board.getOrderIndex() );
        boardMenuResponse.isAdminOnly( board.getIsAdminOnly() );

        return boardMenuResponse.build();
    }

    @Override
    public BoardChildrenResponse toChildrenResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardChildrenResponse.BoardChildrenResponseBuilder boardChildrenResponse = BoardChildrenResponse.builder();

        boardChildrenResponse.parentId( entityParentId( board ) );
        boardChildrenResponse.id( board.getId() );
        boardChildrenResponse.name( board.getName() );
        boardChildrenResponse.type( board.getType() );
        boardChildrenResponse.orderIndex( board.getOrderIndex() );
        boardChildrenResponse.isAdminOnly( board.getIsAdminOnly() );

        return boardChildrenResponse.build();
    }

    @Override
    public List<BoardChildrenResponse> toChildrenList(List<Board> boards) {
        if ( boards == null ) {
            return null;
        }

        List<BoardChildrenResponse> list = new ArrayList<BoardChildrenResponse>( boards.size() );
        for ( Board board : boards ) {
            list.add( toChildrenResponse( board ) );
        }

        return list;
    }

    private Long entityParentId(Board board) {
        if ( board == null ) {
            return null;
        }
        Board parent = board.getParent();
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
