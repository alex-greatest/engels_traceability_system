package com.rena.application.entity.model.traceability.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "station_router")
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