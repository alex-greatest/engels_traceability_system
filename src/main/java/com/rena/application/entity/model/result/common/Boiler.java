package com.rena.application.entity.model.result.common;

import com.rena.application.entity.model.result.order.BoilerOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler", indexes = {
        @Index(name = "idx_boiler_boiler_type_id", columnList = "boiler_type_id"),
        @Index(name = "idx_boiler_date_create", columnList = "date_create"),
        @Index(name = "idx_boiler_date_update", columnList = "date_update"),
        @Index(name = "idx_boiler_user_id", columnList = "user_id"),
        @Index(name = "idx_boiler_boiler_order_id", columnList = "boiler_order_id")
})
public class Boiler {
    @Id
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @NotNull
    @Column(name = "boiler_type_id", nullable = false)
    private Long boilerTypeId;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_order_id", nullable = false)
    private BoilerOrder boilerOrder;
}