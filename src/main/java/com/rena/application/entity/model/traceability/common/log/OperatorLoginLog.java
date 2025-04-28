package com.rena.application.entity.model.traceability.common.log;

import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.vaadin.hilla.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "operator_login_log", indexes = {
        @Index(name = "idx_operator_login_log", columnList = "user_history_id"),
        @Index(name = "idx_operator_login_log_station_id", columnList = "station_id"),
        @Index(name = "idx_operator_login_log_is_login", columnList = "is_login")
})
public class OperatorLoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @NotNull
    @Column(name = "date_login", nullable = false)
    private LocalDateTime dateLogin;

    @Column(name = "date_logout")
    private LocalDateTime dateLogout;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @NotNull
    @Column(name = "is_login", nullable = false)
    private Boolean isLogin = false;
}