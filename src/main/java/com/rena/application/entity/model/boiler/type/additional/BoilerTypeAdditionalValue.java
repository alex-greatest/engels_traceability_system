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
@Table(name = "boiler_type_additional_value", indexes = {
        @Index(name = "idx_boiler_type_additional_value_boiler_type_data", columnList = "boiler_type_additional_data_id"),
        @Index(name = "idx_boiler_type_additional_value_data_set", columnList = "boiler_type_additional_data_set_id")
})
public class BoilerTypeAdditionalValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "value", length = 100, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String value;

    @Column(name = "unit", length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String unit;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_type_additional_data_id" ,nullable = false)
    private BoilerTypeAdditionalData boilerTypeAdditionalData;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "boiler_type_additional_data_set_id", nullable = false)
    private BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet;
}