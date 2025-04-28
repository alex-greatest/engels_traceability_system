package com.rena.application.entity.model.settings.component.material;

import com.rena.application.entity.model.traceability.common.station.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "material_station", uniqueConstraints = {
        @UniqueConstraint(name = "uc_material_station_order", columnNames = {"station_id", "material_id", "order_material"}),
        @UniqueConstraint(name = "uc_material_station", columnNames = {"station_id", "material_id"})
})
public class MaterialStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "order_material", nullable = false)
    private Integer order;
}