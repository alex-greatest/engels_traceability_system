package com.rena.application.repository.boiler_type;

import com.rena.application.entity.model.boiler.BoilerTypeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoilerTypeHistoryRepository extends JpaRepository<BoilerTypeHistory, Long> {
    Optional<BoilerTypeHistory> findByBoilerIdAndIsActive(Long boilerId, Boolean isActive);
}