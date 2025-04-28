package com.rena.application.entity.model.traceability.common.station;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "station", uniqueConstraints = {
        @UniqueConstraint(name = "uc_station_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uc_station_description", columnNames = {"description"})
})
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @Column(name = "description", nullable = false, unique = true, length = 50)
    private String description;
}