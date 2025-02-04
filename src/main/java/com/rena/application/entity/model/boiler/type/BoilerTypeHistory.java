package com.rena.application.entity.model.boiler.type;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSetHistory;
import com.rena.application.entity.model.component.ComponentNameSetHistory;
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
@Table(name = "boiler_type_history", indexes = {
        @Index(name = "idx_boiler_type_history_article_model_is_active", columnList = "model, is_active"),
        @Index(name = "idx_boiler_type_history_name_type_name", columnList = "type_name"),
        @Index(name = "idx_boiler_type_history_component_name_set", columnList = "component_name_set_history_id"),
        @Index(name = "idx_boiler_type_history_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_boiler_type_history_boiler_type_additional_set_id", columnList = "boiler_type_additional_data_set_history_id")
})
public class BoilerTypeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_id", nullable = false)
    private Long boilerId;

    @NotNull
    @Column(name = "type_name", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String typeName;

    @NotNull
    @Column(name = "model", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String model;

    @Column(name = "old_type_name", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String odlTypeName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_name_set_history_id", nullable = false)
    private ComponentNameSetHistory componentNameSetHistory;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boiler_type_additional_data_set_history_id", nullable = false)
    private BoilerTypeAdditionalDataSetHistory boilerTypeAdditionalDataSetHistory;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;
}