package com.rena.application.repository.component.set;

import com.rena.application.entity.model.component.set.ComponentNameSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface ComponentNameSetRepository extends JpaRepository<ComponentNameSet, Long> {
}