package com.rena.application.entity.model.component;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "component_value", indexes = {
        @Index(name = "idx_component_value", columnList = "component_id")
})
public class ComponentValue {
    @Id
    @Column(name = "id", nullable = false, length = 100)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "component_id", nullable = false)
    private ComponentHistory component;

    @Column(name = "value", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String value;
}