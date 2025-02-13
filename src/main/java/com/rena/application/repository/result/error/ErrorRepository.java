package com.rena.application.repository.result.error;

import com.rena.application.entity.model.result.error.Error;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<Error, Long> {
}