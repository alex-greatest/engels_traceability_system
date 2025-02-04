package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoilerTypeAdditionalDataSetHistoryRepository extends JpaRepository<BoilerTypeAdditionalDataSetHistory, Long> {
    Optional<BoilerTypeAdditionalDataSetHistory> findByBoilerTypeAdditionDataSetIdAndIsActive(Long boilerTypeAdditionDataSetId, Boolean isActive);
}