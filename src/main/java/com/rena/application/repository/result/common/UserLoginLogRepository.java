package com.rena.application.repository.result.common;

import com.rena.application.entity.model.result.station.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {


}