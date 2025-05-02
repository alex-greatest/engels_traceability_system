package com.rena.application.entity.model.traceability.common.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_login_log", indexes = {
        @Index(name = "idx_admin_login_log", columnList = "user_history_id"),
        @Index(name = "idx_admin_login_log_station_id", columnList = "station_id"),
        @Index(name = "idx_admin_login_log_is_login", columnList = "is_login")
})
public class AdminLoginLog extends UserLoginLog {
}