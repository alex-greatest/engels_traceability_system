package com.rena.application.entity.model.component;

import com.rena.application.entity.model.result.common.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_binding", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_binding_station_component", columnNames = {"station_id", "component_type_id"}),
        @UniqueConstraint(name = "uc_component_binding_station_order", columnNames = {"station_id", "order"})
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

    @Column(name = "order", nullable = false)
    private Integer order;
}