package com.rena.application.entity.model.traceability.common.station;

import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "operation", indexes = {
        @Index(name = "idx_operation_date_create", columnList = "date_create"),
        @Index(name = "idx_operation", columnList = "boiler_serial_number"),
        @Index(name = "idx_operation_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_operation_is_last", columnList = "is_last")
})
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @NotNull
    @Column(name = "number_shift", nullable = false)
    private Integer numberShift;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_serial_number", nullable = false)
    private Boiler boiler;

    @Column(name = "reason_for_stopping")
    private String reasonForStopping;

    @NotNull
    @Column(name = "is_last", nullable = false)
    private Boolean isLast = false;
}