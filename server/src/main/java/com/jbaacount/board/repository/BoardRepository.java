package com.jbaacount.board.repository;

import com.jbaacount.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>
{
    Optional<Board> findBoardByName(String name);

    @Query("select b from Board b where b.orderIndex between :start and :end")
    List<Board> findAllBetween(@Param("start") Long start, @Param("end") Long end);
}
