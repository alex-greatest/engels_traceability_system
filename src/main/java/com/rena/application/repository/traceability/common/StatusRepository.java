package com.rena.application.repository.traceability.common;

import com.rena.application.entity.model.traceability.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
}