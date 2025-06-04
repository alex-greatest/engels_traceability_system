package com.rena.application.repository.traceability.label;

import com.rena.application.entity.model.traceability.label.LabelStationActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LabelStationActiveRepository extends JpaRepository<LabelStationActive, Long> {
    @Query("select l from LabelStationActive l join l.station join l.labelType where l.station.name = ?1")
    Optional<LabelStationActive> findByStation_Name(String name);

}