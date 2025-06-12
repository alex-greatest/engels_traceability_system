package com.rena.application.repository.settings.material;

import com.rena.application.entity.model.settings.material.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
    @Query("select m from MaterialType m where m.code = ?1 and m.name = ?2")
    Optional<MaterialType> findByCodeAndName(String code, String name);

    @Query(value = "SELECT mt.name " +
            "FROM material_type mt " +
            "JOIN material m ON m.material_type_id = mt.id " +
            "JOIN material_station ms ON ms.material_type_id = m.id " +
            "JOIN station s ON ms.station_id = s.id " +
            "WHERE s.name = ?1 " +
            "GROUP BY mt.name " +
            "ORDER BY MIN(ms.order_material)", nativeQuery = true)
    List<String> findDistinctMaterialTypeNamesByStationName(String stationName);


}