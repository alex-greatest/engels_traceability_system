package com.rena.application.repository.traceability.common.log;

import com.rena.application.entity.model.traceability.common.log.AdminLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AdminLoginLogRepository extends JpaRepository<AdminLoginLog, Long> {
    @Query("select a from AdminLoginLog a join a.station join a.user where a.station.name = ?1 and a.isLogin = true")
    Optional<AdminLoginLog> findByStation_Name(String name);

    @Query("select a from AdminLoginLog a join a.user join a.station where a.user.id = ?1 and a.isLogin = true")
    Optional<AdminLoginLog> findByUserHistory_UserId(Long userId);


}