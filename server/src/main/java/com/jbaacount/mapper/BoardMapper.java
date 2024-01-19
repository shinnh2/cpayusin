package com.jbaacount.mapper;

import com.jbaacount.model.Board;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.request.CategoryUpdateRequest;
import com.jbaacount.payload.response.BoardChildrenResponse;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BoardMapper
{
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board toBoardEntity(BoardCreateRequest request);

    void updateBoard(BoardUpdateRequest request, @MappingTarget Board board);

    void updateBoard(CategoryUpdateRequest request, @MappingTarget Board board);


    @Mapping(target = "parentId", source = "parent.id")
    BoardResponse boardToResponse(Board entity);

    List<BoardResponse> toBoardResponseList(List<Board> boards);


    List<BoardMenuResponse> toBoardMenuResponse(List<Board> boards);

    BoardMenuResponse toMenuResponse(Board board);

    @Mapping(target = "parentId", source = "parent.id")
    BoardChildrenResponse toChildrenResponse(Board board);

    List<BoardChildrenResponse> toChildrenList(List<Board> boards);
}
