package com.rena.application.repository.settings.type;

import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoilerTypeCycleRepository extends JpaRepository<BoilerTypeCycle, Long> {
    Optional<BoilerTypeCycle> findByArticleAndIsActive(String article, Boolean isActive);

    Optional<BoilerTypeCycle> findByBoilerTypeIdAndIsActive(Long boilerTypeId, Boolean isActive);
}