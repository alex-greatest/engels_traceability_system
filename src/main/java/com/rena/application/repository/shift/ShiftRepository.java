package com.rena.application.repository.shift;

import com.rena.application.entity.model.shift.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByNumber(Integer number);
}