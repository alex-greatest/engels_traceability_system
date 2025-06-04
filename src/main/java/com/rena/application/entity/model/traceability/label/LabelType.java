package com.rena.application.entity.model.traceability.label;

import com.rena.application.entity.model.traceability.common.station.StationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "label_type", uniqueConstraints = {
        @UniqueConstraint(name = "uc_label_type_name_name_station_type_id", columnNames = {"name", "station_type_id"})
})
public class LabelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_type_id", nullable = false)
    private StationType stationType;
}