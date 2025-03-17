package com.rena.application.repository.settings;

import com.rena.application.entity.model.result.common.Station;
import com.rena.application.entity.model.settings.PartLast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface PartLastRepository extends JpaRepository<PartLast, Long> {
    @Transactional
    @Modifying
    @Query("update PartLast p set p.part_id = ?1 where p.station.name = ?2")
    void updatePart_idByStation(String part_id, String name);

    @Query("select p from PartLast p JOIN  p.station where p.station.name = ?1")
    Optional<PartLast> findByStation_Name(String name);
}