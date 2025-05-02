package com.rena.application.entity.model.traceability.common.log;

import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.station.Station;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class UserLoginLog {
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

    @Column(name = "is_login", nullable = false)
    private Boolean isLogin = false;
}