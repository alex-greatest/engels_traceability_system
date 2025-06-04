package com.rena.application.repository.traceability.common.boiler;

import com.rena.application.entity.model.traceability.station.order.BoilerLabelCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface BoilerLabelCountRepository extends JpaRepository<BoilerLabelCount, Long> {
    @Query("select b from BoilerLabelCount b join b.boiler where b.boiler.serialNumber = ?1")
    Optional<BoilerLabelCount> findByBoiler_SerialNumber(String serialNumber);

}