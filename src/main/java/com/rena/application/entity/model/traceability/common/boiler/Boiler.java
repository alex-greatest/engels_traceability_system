package com.rena.application.entity.model.traceability.common.boiler;

import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler", indexes = {
        @Index(name = "idx_boiler_date_create", columnList = "date_create"),
        @Index(name = "idx_boiler_date_update", columnList = "date_update"),
        @Index(name = "idx_boiler_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_boiler_boiler_type_cycle_id", columnList = "boiler_type_cycle_id"),
        @Index(name = "idx_boiler_boiler_order_id", columnList = "boiler_order_id")
})
public class Boiler {
    @Id
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_type_cycle_id", nullable = false)
    private BoilerTypeCycle boilerTypeCycle;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station lastStation;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_order_id", nullable = false)
    private BoilerOrder boilerOrder;
}