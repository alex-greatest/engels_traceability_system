package com.rena.application.entity.model.boiler.type;

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
        @Index(name = "idx_boiler_type_history_user_idd", columnList = "user_history_id"),
        @Index(name = "idx_boiler_type_history_component_name_set_id", columnList = "component_name_set_id"),
        @Index(name = "idx_boiler_type_history_boiler_set", columnList = "boiler_type_additional_data_set_id"),
        @Index(name = "idx_boiler_type_history_type_operation", columnList = "type_operation"),
        @Index(name = "idx_boiler_type_history_article", columnList = "article")
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
    @Column(name = "article", nullable = false, length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String article;

    @NotNull
    @Column(name = "component_name_set_id", nullable = false)
    private Long componentNameSetId;

    @NotNull
    @Column(name = "boiler_type_additional_data_set_id", nullable = false)
    private Long boilerTypeAdditionalDataSetId;

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
    @Column(name = "user_history_id", nullable = false)
    private Long userId;
}