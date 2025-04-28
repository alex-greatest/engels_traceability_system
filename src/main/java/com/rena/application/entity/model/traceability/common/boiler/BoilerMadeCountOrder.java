package com.rena.application.entity.model.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "boiler_made_count_order")
public class BoilerMadeCountOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_order_id", nullable = false)
    private BoilerOrder boilerOrder;

    @Column(name = "amount_boiler_made_order", nullable = false)
    private Integer amountBoilerMadeOrder;
}