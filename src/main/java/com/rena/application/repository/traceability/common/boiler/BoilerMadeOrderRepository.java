package com.rena.application.repository.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.BoilerMadeCountOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoilerMadeOrderRepository extends JpaRepository<BoilerMadeCountOrder, Long> {
    @Query("select b from BoilerMadeCountOrder b join b.station join b.boilerOrder where b.station.name = ?1 and b.boilerOrder.id = ?2")
    Optional<BoilerMadeCountOrder> findByStation_NameAndBoilerOrder_Id(String name, String boilerOrderId);

    Optional<BoilerMadeCountOrder> findByStation_Name(String name);
}