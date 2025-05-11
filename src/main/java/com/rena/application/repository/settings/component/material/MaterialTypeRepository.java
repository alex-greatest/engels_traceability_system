package com.rena.application.repository.settings.component.material;

import com.rena.application.entity.model.settings.component.material.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
    @Query(value = "SELECT mt.name " +
            "FROM material_type mt " +
            "JOIN material m ON m.material_type_id = mt.id " +
            "JOIN material_station ms ON ms.material_id = m.id " +
            "JOIN station s ON ms.station_id = s.id " +
            "WHERE s.name = ?1 " +
            "ORDER BY ms.order_material", nativeQuery = true)
    List<String> findDistinctMaterialTypeNamesByStationName(String stationName);
}