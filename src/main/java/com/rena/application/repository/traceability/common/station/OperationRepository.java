package com.rena.application.repository.traceability.common.station;

import com.rena.application.entity.dto.traceability.common.router.StationRouteCheckResult;
import com.rena.application.entity.model.traceability.common.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT o FROM Operation o " +
            "JOIN o.stationHistory " +
            "JOIN o.boiler " +
            "WHERE o.stationHistory.name = ?1 " +
            "AND o.status = ?2 " +
            "AND o.id = (SELECT MAX(op.id) FROM Operation op " +
            "            JOIN op.stationHistory sh " +
            "            WHERE sh.name = ?1 AND op.status = ?2)")
    Optional<Operation> findLatestActiveByStationName(String stationName, Integer status);

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