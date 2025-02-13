package com.rena.application.repository;

import com.rena.application.entity.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByNumber(Long number);
}