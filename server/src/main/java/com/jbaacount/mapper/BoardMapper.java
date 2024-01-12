package com.jbaacount.mapper;

import com.jbaacount.model.Board;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.response.BoardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BoardMapper
{
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board toBoardEntity(BoardCreateRequest request);
    BoardResponse boardToResponse(Board entity);

}
