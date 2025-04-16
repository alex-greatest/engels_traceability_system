package com.rena.application.repository.traceability.common;

import com.rena.application.entity.model.traceability.common.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT o FROM Operation o " +
           "JOIN FETCH o.station " +
           "JOIN FETCH o.status " +
           "JOIN FETCH o.boiler " +
           "WHERE o.boiler.serialNumber = :serialNumber " +
           "ORDER BY o.dateCreate DESC")
    List<Operation> findAllByBoilerSerialNumber(@Param("serialNumber") String serialNumber);

    @Query("select o from Operation o " +
            "JOIN o.station JOIN o.status JOIN o.boiler" +
            " where o.status.id = ?1 and o.station.name = ?2 and o.boiler.serialNumber = ?3 and o.isLast = true")
    Optional<Operation> findByStatus_IdAndStation_NameAndIsLastTrue(Long id, String name, String serialNumber);

    @Query("select o from Operation o JOIN o.station JOIN o.status JOIN o.boiler.boilerTypeCycle where o.id = ?1 and o.isLast = true")
    Optional<Operation> findOperationById(Long id);

    @Query("select o from Operation o " +
            "JOIN o.station JOIN o.status JOIN o.boiler" +
            " where o.station.name = ?1 and o.boiler.serialNumber = ?2 and o.isLast = true")
    Optional<Operation> findByStation_NameAndIsLastTrue(String name, String serialNumber);
}
