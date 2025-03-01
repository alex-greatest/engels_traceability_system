package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentNameSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface ComponentNameSetRepository extends JpaRepository<ComponentNameSet, Long> {
}