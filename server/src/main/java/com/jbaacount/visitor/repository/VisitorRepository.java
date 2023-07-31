package com.jbaacount.visitor.repository;

import com.jbaacount.visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface VisitorRepository extends JpaRepository<Visitor, Long>
{
    boolean existsByIpAddressAndDate(String ipAddress, LocalDate date);

    Long countByDate(LocalDate date);

}
