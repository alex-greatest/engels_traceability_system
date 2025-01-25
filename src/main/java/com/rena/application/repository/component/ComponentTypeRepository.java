package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {
    Optional<ComponentType> findComponentTypeByName(String name);
}