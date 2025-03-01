package com.rena.application.repository.result.common;

import com.rena.application.entity.model.result.station.wp.one.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}