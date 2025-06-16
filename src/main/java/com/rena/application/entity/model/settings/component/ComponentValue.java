package com.rena.application.entity.model.settings.component;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_value", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_value_value", columnNames = {"value"}),
        @UniqueConstraint(name = "uc_component_value_code_code_boiler_serialNumber", columnNames = {"code", "boiler_serial_number"})
})
public class ComponentValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_serial_number", nullable = false)
    private Boiler boiler;
}