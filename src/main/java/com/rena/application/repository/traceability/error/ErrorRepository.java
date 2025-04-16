package com.rena.application.repository.traceability.error;

import com.rena.application.entity.model.traceability.error.Error;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<Error, Long> {
}