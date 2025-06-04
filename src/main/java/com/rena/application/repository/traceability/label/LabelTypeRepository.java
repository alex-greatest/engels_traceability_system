package com.rena.application.repository.traceability.label;

import com.rena.application.entity.model.traceability.label.LabelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LabelTypeRepository extends JpaRepository<LabelType, Long> {
    @Query("select l from LabelType l join l.stationType where l.name = ?1")
    Optional<LabelType> findByName(String name);

    @Query("select l from LabelType l join l.stationType where l.stationType.name = ?1")
    List<LabelType> findByStationType_Name(String name);
}