package com.rena.application.repository.traceability.common;

import com.rena.application.entity.model.traceability.station.SerialNumberLogManual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerialNumberLogManualRepository extends JpaRepository<SerialNumberLogManual, Long> {
}