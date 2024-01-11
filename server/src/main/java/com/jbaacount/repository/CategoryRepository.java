package com.jbaacount.repository;

import com.jbaacount.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom
{
    Optional<Category> findByName(String name);

    @Query("select c from Category c where c.orderIndex between :start and :end")
    List<Category> findAllBetween(@Param("start") Long start, @Param("end") Long end);
}
