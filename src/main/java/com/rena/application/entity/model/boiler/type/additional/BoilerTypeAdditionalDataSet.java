package com.rena.application.entity.model.boiler.type.additional;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "boiler_type_additional_data_set", uniqueConstraints = {
        @UniqueConstraint(name = "uc_boiler_type_additional_data_set", columnNames = {"name"})
})
public class BoilerTypeAdditionalDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, length = 100)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}