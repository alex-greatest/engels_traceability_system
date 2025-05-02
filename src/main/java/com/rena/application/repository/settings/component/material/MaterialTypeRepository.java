package com.rena.application.repository.settings.component.material;

import com.rena.application.entity.model.settings.component.material.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
    @Query("SELECT DISTINCT mt.name FROM MaterialType mt " +
            "JOIN Material m ON m.materialType.id = mt.id " +
            "JOIN MaterialStation ms ON ms.material.id = m.id " +
            "JOIN ms.station s " +
            "WHERE s.name = ?1 " +
            "ORDER BY mt.name") // Опционально: сортировка имен типов
    List<String> findDistinctMaterialTypeNamesByStationName(String stationName);
}