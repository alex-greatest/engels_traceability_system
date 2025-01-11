package com.rena.application.repository;

import com.rena.application.entity.model.component.ComponentValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentValueHistoryRepository extends JpaRepository<ComponentValueHistory, String> {
}