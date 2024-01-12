package com.jbaacount.repository;

import com.jbaacount.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom
{
    Optional<Board> findBoardByName(String name);


}
