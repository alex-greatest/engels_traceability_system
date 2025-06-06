package com.rena.application.repository.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.BoilerOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoilerOrderHistoryRepository extends JpaRepository<BoilerOrderHistory, Long> {
}