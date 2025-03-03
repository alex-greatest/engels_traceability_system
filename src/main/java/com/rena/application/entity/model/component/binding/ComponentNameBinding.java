package com.rena.application.entity.model.component.binding;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_binding_name", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_name_binding_name", columnNames = {"name"})
})
public class ComponentNameBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}