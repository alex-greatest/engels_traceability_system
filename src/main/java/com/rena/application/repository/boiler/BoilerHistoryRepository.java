package com.rena.application.repository.boiler;

import com.rena.application.entity.model.boiler.BoilerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoilerHistoryRepository extends JpaRepository<BoilerHistory, Long> {
    Optional<BoilerHistory> findByBoilerIdAndIsActive(Long boilerId, Boolean isActive);
}