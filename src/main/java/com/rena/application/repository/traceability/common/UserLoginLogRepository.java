package com.rena.application.repository.traceability.common;

import com.rena.application.entity.model.traceability.station.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {


}