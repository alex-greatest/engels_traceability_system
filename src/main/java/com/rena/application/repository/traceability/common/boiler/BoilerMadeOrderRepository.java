package com.rena.application.repository.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.BoilerMadeCountOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoilerMadeOrderRepository extends JpaRepository<BoilerMadeCountOrder, Long> {
    Optional<BoilerMadeCountOrder> findByStation_Name(String name);
}