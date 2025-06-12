package com.rena.application.repository.settings.material;

import com.rena.application.entity.model.settings.material.MaterialStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MaterialStationRepository extends JpaRepository<MaterialStation, Long> {
    @Query("select m from MaterialStation m join m.materialType join m.station where m.station.name = ?1 order by m.order")
    List<MaterialStation> findByStation_Name(String name);

}