package com.rena.application.entity.model.traceability.station.order;

import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import com.rena.application.entity.model.settings.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler_order", indexes = {
        @Index(name = "idx_boiler_order_date", columnList = "date"),
        @Index(name = "idx_boiler_order_order_number", columnList = "order_number"),
        @Index(name = "idx_boiler_order", columnList = "boiler_type_cycle_id"),
        @Index(name = "idx_boiler_order_user_history_id", columnList = "user_history_id")
})
public class BoilerOrder {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "scan_code")
    private String scanCode;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boiler_type_cycle_id")
    private BoilerTypeCycle boilerTypeCycle;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @NotNull
    @Column(name = "amount_boiler_order", nullable = false)
    private Integer amountBoilerOrder;

    @NotNull
    @Column(name = "amount_boiler_print", nullable = false)
    private Integer amountBoilerPrint;

    @NotNull
    @Column(name = "amount_boiler_made", nullable = false)
    private Integer amountBoilerMade;

    @NotNull
    @Column(name = "number_shift_created", nullable = false)
    private Integer numberShiftCreated;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}