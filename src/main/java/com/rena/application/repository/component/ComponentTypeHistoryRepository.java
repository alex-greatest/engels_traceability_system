package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentTypeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComponentTypeHistoryRepository extends JpaRepository<ComponentTypeHistory, Long> {
    Optional<ComponentTypeHistory> findByNameAndIsActive(String name, Boolean isActive);

}