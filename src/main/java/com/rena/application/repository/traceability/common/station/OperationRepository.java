package com.rena.application.repository.traceability.common.station;

import com.rena.application.entity.dto.traceability.station.router.StationRouteCheckResult;
import com.rena.application.entity.model.traceability.common.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT o FROM Operation o " +
           "JOIN FETCH o.stationHistory " +
           "JOIN FETCH o.boiler " +
           "WHERE o.boiler.serialNumber = :serialNumber " +
           "ORDER BY o.dateCreate DESC")
    List<Operation> findAllByBoilerSerialNumber(@Param("serialNumber") String serialNumber);

    @Query("select o from Operation o " +
            "JOIN o.stationHistory JOIN o.boiler" +
            " where o.status = ?1 and o.stationHistory.name = ?2 and o.boiler.serialNumber = ?3")
    Optional<Operation> findByStatus_IdAndStation_NameAndIsLastTrue(Integer status, String name, String serialNumber);

    @Query("select o from Operation o JOIN o.stationHistory JOIN o.boiler.boilerTypeCycle where o.id = ?1")
    Optional<Operation> findOperationById(Long id);

    @Query(nativeQuery = true, value =
            "SELECT is_ok AS isOk, message, not_passed_stations AS notPassedStations, failed_stations AS failedStations " +
                    "FROM check_station_route(?1, ?2, ?3, ?4)")
    StationRouteCheckResult checkStationRoute(
            String serialNumber,
            String stationName,
            String reworkStationName,
            Integer okStatus
    );
}