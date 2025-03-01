package com.rena.application.repository.result.station.wp.one.order;

import com.rena.application.entity.model.result.station.wp.one.Boiler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoilerRepository extends JpaRepository<Boiler, Long> {
    @Query("select b from Boiler b JOIN b.boilerTypeCycle JOIN  b.boilerOrder where b.boilerOrder.id = ?1")
    Page<Boiler> findByBoilerOrder_OrderNumber(String id, Pageable pageable);
}