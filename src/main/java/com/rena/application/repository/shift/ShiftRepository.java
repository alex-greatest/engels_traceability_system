package com.rena.application.repository.shift;

import com.rena.application.entity.model.shift.Shift;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@JaversSpringDataAuditable
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByNumber(Integer number);
}