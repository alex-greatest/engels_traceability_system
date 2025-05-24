package com.rena.application.entity.model.traceability.common.station;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "station_router", indexes = {
        @Index(name = "idx_station_router_order", columnList = "order")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_station_router_station_id", columnNames = {"station_id"})
})
public class StationRouter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", unique = true, nullable = false)
    private Station station;

    @Column(name = "order", nullable = false, unique = true)
    private Integer order;
}