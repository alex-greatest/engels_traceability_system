package com.rena.application.repository;

import com.rena.application.entity.model.component.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long> {
}