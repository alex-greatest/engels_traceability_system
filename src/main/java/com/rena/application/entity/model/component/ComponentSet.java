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
@Table(name = "component_set", indexes = {
        @Index(name = "idx_component_set", columnList = "component_id"),
        @Index(name = "idx_component_name_set", columnList = "component_name_set_id"),
        @Index(name = "idx_component_set_value", columnList = "value")
})
public class ComponentSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "component_name_set_id")
    private ComponentNameSet componentNameSet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "component_id")
    private Component component;

    @NotNull
    @Column(name = "value", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String value;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}