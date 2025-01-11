package com.rena.application.repository;

import com.rena.application.entity.model.component.ComponentValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentValueRepository extends JpaRepository<ComponentValue, String> {
}