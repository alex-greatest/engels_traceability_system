package com.rena.application.entity.model.component.binding;

import com.rena.application.entity.model.component.ComponentType;
import com.rena.application.entity.model.component.set.ComponentNameSet;
import com.rena.application.entity.model.traceability.common.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_binding", uniqueConstraints = {
        @UniqueConstraint(name = "uc_binding_name_set", columnNames = {"component_name_set_id", "component_type_id"}),
        @UniqueConstraint(name = "uc_binding_order", columnNames = {"component_name_set_id", "station_id", "order_component"})
})
public class ComponentBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_name_set_id", nullable = false)
    private ComponentNameSet componentNameSet;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_type_id", nullable = false)
    private ComponentType componentType;

    @Column(name = "order_component", nullable = false)
    private Integer order;
}