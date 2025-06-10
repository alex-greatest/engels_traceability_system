package com.rena.application.entity.model.settings.component;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_value")
public class ComponentValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "component_type_id")
    private ComponentType componentType;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boiler_serial_number")
    private Boiler boiler;
}