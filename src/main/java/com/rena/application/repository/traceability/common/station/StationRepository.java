package com.rena.application.repository.traceability.common.station;

import com.rena.application.entity.model.traceability.common.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    boolean existsByName(String name);

    @Query("select s from Station s join s.stationType where s.name = ?1")
    Optional<Station> findByName(String name);
}