package com.rena.application.repository.traceability.common.log;

import com.rena.application.entity.model.traceability.station.order.SerialNumberLogManual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerialNumberLogManualRepository extends JpaRepository<SerialNumberLogManual, Long> {
}