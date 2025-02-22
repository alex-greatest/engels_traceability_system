package com.rena.application.entity.model.result.order;

import com.rena.application.entity.model.result.common.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler_order", indexes = {
        @Index(name = "idx_boiler_order_user_id", columnList = "user_id"),
        @Index(name = "idx_boiler_order_date", columnList = "date"),
        @Index(name = "idx_boiler_order_boiler_type_id", columnList = "boiler_type_id"),
        @Index(name = "idx_boiler_order_order_number", columnList = "order_number"),
        @Index(name = "idx_boiler_order_number_shift_created", columnList = "number_shift_created")
})
public class BoilerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @NotNull
    @Column(name = "boiler_type_id", nullable = false)
    private Long boilerTypeId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

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
    @Column(name = "scan_code", nullable = false)
    private String scanCode;

    @NotNull
    @Column(name = "number_shift_created", nullable = false)
    private Integer numberShiftCreated;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}