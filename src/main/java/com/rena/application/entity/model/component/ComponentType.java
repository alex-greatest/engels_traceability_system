package com.rena.application.entity.model.component;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "component_type", uniqueConstraints = {
        @UniqueConstraint(name = "uc_component_type_name", columnNames = {"name"})
})
public class ComponentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}