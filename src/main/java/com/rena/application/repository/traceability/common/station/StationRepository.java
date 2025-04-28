package com.rena.application.repository.traceability.common.station;

import com.rena.application.entity.model.traceability.common.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByDescription(String description);

    Optional<Station> findByName(String name);
}