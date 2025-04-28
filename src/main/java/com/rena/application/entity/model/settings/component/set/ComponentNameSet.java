package com.rena.application.entity.model.settings.component.set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_name_set", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_name_set_name", columnNames = {"name"})
})
public class ComponentNameSet {
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