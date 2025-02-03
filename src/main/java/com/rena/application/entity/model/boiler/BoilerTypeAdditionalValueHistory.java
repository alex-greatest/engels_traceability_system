package com.rena.application.entity.model.boiler;

import com.rena.application.entity.model.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler_type_additional_value_history", indexes = {
        @Index(name = "idx_boiler_type_additional_value_history", columnList = "boiler_type_additional_data_set_history_id"),
        @Index(name = "idx_boiler_type_additional_value_history_boiler_type_additional_id", columnList = "boiler_type_additional_data_id"),
        @Index(name = "idx_boiler_type_additional_value_history_is_active", columnList = "is_active")
})
public class BoilerTypeAdditionalValueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_type_additional_value_id", nullable = false)
    private Long boilerTypeAdditionalValue;

    @NotNull
    @Column(name = "value", length = 100, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String value;

    @Column(name = "old_value", length = 100)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String oldValue;

    @Column(name = "unit", length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String unit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_type_additional_data_id", nullable = false)
    private BoilerTypeAdditionalData boilerTypeAdditionalData;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_type_additional_data_set_history_id", nullable = false)
    private BoilerTypeAdditionalDataSetHistory boilerTypeAdditionalDataSet;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @NotNull
    @Column(name = "type_operation", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer typeOperation;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @JdbcTypeCode(SqlTypes.BIT)
    private Boolean isActive = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;
}