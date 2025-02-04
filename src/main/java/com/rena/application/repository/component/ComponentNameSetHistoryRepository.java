package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentNameSetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComponentNameSetHistoryRepository extends JpaRepository<ComponentNameSetHistory, Long> {
    Optional<ComponentNameSetHistory> findByComponentNameSetIdAndIsActive(Long componentNameSetId, Boolean isActive);

    Optional<ComponentNameSetHistory> findByNameAndIsActive(String name, Boolean isActive);
}