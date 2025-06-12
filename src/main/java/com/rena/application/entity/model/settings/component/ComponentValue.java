package com.rena.application.entity.model.settings.component;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_value", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_value", columnNames = {"component_type_id", "boiler_serial_number"}),
        @UniqueConstraint(name = "uc_component_value_value", columnNames = {"value"})
})
public class ComponentValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_type_id", nullable = false)
    private ComponentType componentType;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_serial_number", nullable = false)
    private Boiler boiler;
}