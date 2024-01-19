package com.jbaacount.repository;

import com.jbaacount.model.Board;
import com.jbaacount.payload.response.BoardTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom
{

    @Query("SELECT b FROM Board b WHERE b.parent.id = :parentId")
    List<Board> findBoardByParentBoardId(@Param("parentId") Long parentId);

    @Query("SELECT Count(b) FROM Board b WHERE b.parent.id = :parentId")
    Integer countChildrenByParentId(@Param("parentId") Long parentId);

    @Query("SELECT Count(b) FROM Board b WHERE b.parent IS NULL")
    Integer countParent();


    @Query("SELECT new com.jbaacount.payload.response.BoardTypeResponse(b.id, b.name) FROM Board b " +
            "WHERE b.type = 'Board' " +
            "ORDER BY b.orderIndex")
    List<BoardTypeResponse> findBoardType();

}
