package com.rena.application.repository.traceability.common.router;

import com.rena.application.entity.model.traceability.common.station.StationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface StationHistoryRepository extends JpaRepository<StationHistory, Long> {
    @Query("SELECT s FROM StationHistory s join s.stationType WHERE s.name = :name and s.isActive = true")
    Optional<StationHistory> findByName(String name);
}