package com.rena.application.repository.result.common;

import com.rena.application.entity.model.result.common.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByDescription(String description);

    Optional<Station> findByName(String name);
}