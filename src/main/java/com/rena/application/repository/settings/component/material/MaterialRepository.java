package com.rena.application.repository.settings.component.material;

import com.rena.application.entity.model.settings.component.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT m FROM Material m " +
            "JOIN m.materialType mt " +
            "JOIN MaterialStation ms ON ms.material.id = m.id " +
            "JOIN ms.station s " +
            "WHERE s.name = ?2 " +
            "AND mt.id IN ?1 ")
    List<Material> findAllMaterialsByTypeIdsAndStationOrdered(List<Long> materialTypeIds, String stationName);

    @Query("SELECT m FROM Material m " +
            "JOIN m.materialType mt " +
            "JOIN MaterialStation ms ON ms.material.id = m.id " +
            "JOIN ms.station s " +
            "WHERE s.name = ?1 " +
            "ORDER BY ms.order") // Добавлена сортировка по ms.order
    List<Material> findAllMaterialsByStationOrdered(String stationName);
}