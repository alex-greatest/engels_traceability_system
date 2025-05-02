package com.rena.application.repository.traceability.station.router;

import com.rena.application.entity.model.traceability.common.station.StationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StationHistoryRepository extends JpaRepository<StationHistory, Long> {
    Optional<StationHistory> findByName(String name);
}