package com.rena.application.repository.result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rena.application.entity.model.traceability.station.order.BoilerOrder;

public interface BoilerOrderRepository extends JpaRepository<BoilerOrder, String> {
    @Query("select b from BoilerOrder b JOIN b.userHistory JOIN b.status JOIN b.boilerTypeCycle where b.modifiedDate between ?1 and ?2")
    List<BoilerOrder> findByModifiedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("select b from BoilerOrder b JOIN FETCH b.userHistory JOIN FETCH b.status JOIN FETCH b.boilerTypeCycle where b.scanCode = ?1")
    Optional<BoilerOrder> findByScanCode(String scanCode);
}