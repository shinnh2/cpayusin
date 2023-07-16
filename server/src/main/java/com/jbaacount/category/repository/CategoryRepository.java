package com.jbaacount.category.repository;

import com.jbaacount.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom
{
    Optional<Category> findByName(String name);
}
