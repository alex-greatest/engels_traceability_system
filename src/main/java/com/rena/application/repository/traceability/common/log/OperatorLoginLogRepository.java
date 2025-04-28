package com.rena.application.repository.traceability.common.log;

import com.rena.application.entity.model.traceability.common.log.OperatorLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface OperatorLoginLogRepository extends JpaRepository<OperatorLoginLog, Long> {
    @Query("select o from OperatorLoginLog o join o.station join o.userHistory where o.userHistory.userId = ?1 and o.isLogin = true")
    Optional<OperatorLoginLog> findByUserHistory_Username(Long userId);

    @Query("select o from OperatorLoginLog o join o.station join o.userHistory where o.station.name = ?1 and o.isLogin = true")
    Optional<OperatorLoginLog> findByStation_Name(String stationName);
}