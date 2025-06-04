package com.rena.application.repository.traceability.label;

import com.rena.application.entity.model.traceability.label.LabelValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface LabelValueRepository extends JpaRepository<LabelValue, Long> {
    @Query("select l from LabelValue l join l.labelType where l.labelType.name = ?1 Order by l.variable")
    List<LabelValue> findByLabelType_Name(String name);
}