package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface BoilerTypeAdditionalDataSetRepository extends JpaRepository<BoilerTypeAdditionalDataSet, Long> {
}