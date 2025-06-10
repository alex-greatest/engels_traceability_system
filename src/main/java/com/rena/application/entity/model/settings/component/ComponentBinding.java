package com.rena.application.entity.model.settings.component;

import com.rena.application.entity.model.settings.type.BoilerType;
import com.rena.application.entity.model.traceability.common.station.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_binding", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_binding", columnNames = {"station_id", "component_type_id", "boiler_type_id", "order_component"})
})
public class ComponentBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_type_id", nullable = false)
    private ComponentType componentType;

    @Column(name = "order_component", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boiler_type_id")
    private BoilerType boilerType;
}