package com.rena.application.entity.model.traceability.label;

import com.rena.application.entity.model.traceability.common.station.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "label_station_active", uniqueConstraints = {
        @UniqueConstraint(name = "uc_label_station_active", columnNames = {"station_id"})
})
public class LabelStationActive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "label_type_id", nullable = false)
    private LabelType labelType;
}