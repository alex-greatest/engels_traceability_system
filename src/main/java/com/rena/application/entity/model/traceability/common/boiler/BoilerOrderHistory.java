package com.rena.application.entity.model.traceability.common.boiler;

import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "boiler_order_history")
public class BoilerOrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_serial_number")
    private Boiler boiler;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_order_id")
    private BoilerOrder boilerOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;
}