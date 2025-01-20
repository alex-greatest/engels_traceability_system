package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComponentHistoryRepository extends JpaRepository<ComponentHistory, Long> {
    Optional<ComponentHistory> findByNameAndIsActive(String name, Boolean isActive);
}