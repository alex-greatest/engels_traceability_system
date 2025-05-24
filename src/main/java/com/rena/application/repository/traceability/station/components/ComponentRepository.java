package com.rena.application.repository.traceability.station.components;

import com.rena.application.entity.model.traceability.station.component.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query("SELECT c FROM Component c JOIN FETCH c.status JOIN FETCH c.operation o WHERE o.id = :id ORDER BY c.name ASC")
    List<Component> findByOperation_IdOrderByNameAsc(Long id);

}