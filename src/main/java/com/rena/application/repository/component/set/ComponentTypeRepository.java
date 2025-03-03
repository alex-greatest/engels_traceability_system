package com.rena.application.repository.component.set;

import com.rena.application.entity.model.component.ComponentType;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@JaversSpringDataAuditable
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {
    Optional<ComponentType> findComponentTypeByName(String name);
}