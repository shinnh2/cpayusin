package com.jbaacount.mapper;

import com.jbaacount.model.Board;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.request.CategoryUpdateRequest;
import com.jbaacount.payload.response.BoardChildrenResponse;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
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

        BoardResponse boardResponse = new BoardResponse();

        boardResponse.setParentId( entityParentId( entity ) );
        boardResponse.setId( entity.getId() );
        boardResponse.setName( entity.getName() );
        boardResponse.setOrderIndex( entity.getOrderIndex() );

        return boardResponse;
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

        BoardMenuResponse boardMenuResponse = new BoardMenuResponse();

        boardMenuResponse.setId( board.getId() );
        boardMenuResponse.setName( board.getName() );
        boardMenuResponse.setType( board.getType() );
        boardMenuResponse.setOrderIndex( board.getOrderIndex() );

        return boardMenuResponse;
    }

    @Override
    public BoardChildrenResponse toChildrenResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardChildrenResponse boardChildrenResponse = new BoardChildrenResponse();

        boardChildrenResponse.setParentId( entityParentId( board ) );
        boardChildrenResponse.setId( board.getId() );
        boardChildrenResponse.setName( board.getName() );
        boardChildrenResponse.setType( board.getType() );
        boardChildrenResponse.setOrderIndex( board.getOrderIndex() );

        return boardChildrenResponse;
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
