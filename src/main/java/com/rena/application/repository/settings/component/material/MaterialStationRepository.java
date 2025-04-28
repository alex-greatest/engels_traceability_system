package com.rena.application.repository.settings.component.material;

import com.rena.application.entity.model.settings.component.material.MaterialStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MaterialStationRepository extends JpaRepository<MaterialStation, Long> {
    @Query("SELECT m FROM MaterialStation m " +
            "JOIN m.material mt " +
            "JOIN m.station " +
            "where m.station.name = ?1")
    List<MaterialStation> findAllMaterialsByStation(String stationName);
}