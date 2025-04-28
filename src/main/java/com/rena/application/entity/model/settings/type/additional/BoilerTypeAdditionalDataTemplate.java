package com.rena.application.entity.model.settings.type.additional;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "boiler_type_additional_data_template")
public class BoilerTypeAdditionalDataTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "unit", length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String unit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_type_additional_data_id", nullable = false)
    private BoilerTypeAdditionalData boilerTypeAdditionalData;
}