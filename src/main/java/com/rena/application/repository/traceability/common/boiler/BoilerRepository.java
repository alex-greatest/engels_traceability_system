package com.rena.application.repository.traceability.common.boiler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;

public interface BoilerRepository extends JpaRepository<Boiler, String> {
    @Query("select b from Boiler b JOIN b.boilerTypeCycle JOIN b.boilerOrder where b.boilerOrder.id = ?1")
    Page<Boiler> findByBoilerOrder_OrderNumber(String id, Pageable pageable);

    @Query("select b from Boiler b JOIN b.boilerTypeCycle JOIN b.boilerOrder where b.serialNumber = ?1")
    Optional<Boiler> findBySerialNumber(String serialNumber);
    
    @Query("select b from Boiler b JOIN b.boilerTypeCycle JOIN b.boilerOrder JOIN b.status JOIN b.lastStation JOIN b.userHistory where b.dateCreate between ?1 and ?2")
    List<Boiler> findAllByDateCreateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select b from Boiler b JOIN b.boilerTypeCycle JOIN b.boilerOrder JOIN b.status JOIN b.lastStation JOIN b.userHistory where b.boilerOrder.id = ?1")
    List<Boiler> findAllByBoilerOrderId(Long boilerOrderId);


}