package com.rena.application.repository.settings.shift;

import com.rena.application.entity.model.settings.shift.ShiftStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ShiftStationRepository extends JpaRepository<ShiftStation, Long> {
    @Query("select s from ShiftStation s JOIN s.station where s.station.name = ?1")
    Optional<ShiftStation> findByStationName(String name);
}