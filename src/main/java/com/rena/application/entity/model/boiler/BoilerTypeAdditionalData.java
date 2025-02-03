package com.rena.application.entity.model.boiler;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "boiler_type_additional_data", uniqueConstraints = {
        @UniqueConstraint(name = "uc_boiler_type_additional_data_name", columnNames = {"name"})
})
public class BoilerTypeAdditionalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String name;
}