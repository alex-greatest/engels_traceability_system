package com.rena.application.repository.result;

import com.rena.application.entity.model.result.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}