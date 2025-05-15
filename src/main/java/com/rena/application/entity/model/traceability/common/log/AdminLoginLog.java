package com.rena.application.entity.model.traceability.common.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_login_log", indexes = {
        @Index(name = "idx_admin_login_log_station_id", columnList = "station_id"),
        @Index(name = "idx_admin_login_log_is_login", columnList = "is_login"),
        @Index(name = "idx_admin_login_log_user_id", columnList = "user_id")
})
public class AdminLoginLog extends UserLoginLog {
}