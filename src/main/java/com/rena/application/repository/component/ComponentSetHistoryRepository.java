package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentSetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComponentSetHistoryRepository extends JpaRepository<ComponentSetHistory, Long> {
    Optional<ComponentSetHistory> findByIsActiveAndComponentSetId(Boolean isActive, Long componentSetId);
}