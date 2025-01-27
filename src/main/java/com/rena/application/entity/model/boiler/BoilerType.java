package com.rena.application.entity.model.boiler;

import com.rena.application.entity.model.component.ComponentNameSet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "boiler_type", indexes = {
        @Index(name = "idx_boiler_type_component_name_set", columnList = "component_name_set_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_boiler_type_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uc_boiler_type_article", columnNames = {"article"})
})
public class BoilerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @NotNull
    @Column(name = "article", nullable = false, unique = true, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String article;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_name_set_id", nullable = false)
    private ComponentNameSet componentNameSet;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}