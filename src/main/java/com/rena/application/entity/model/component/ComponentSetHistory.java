package com.rena.application.entity.model.component;

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
@Table(name = "component_set_history", indexes = {
        @Index(name = "idx_component_set_history_component_type", columnList = "component_type_history_id"),
        @Index(name = "idx_component_set_history_component_name", columnList = "component_name_set_history_id"),
        @Index(name = "idx_component_set_history", columnList = "user_history_id"),
        @Index(name = "idx_component_set_history_component_id_is_active", columnList = "component_set_id, is_active"),
        @Index(name = "idx_component_set_history_old_type_set_history", columnList = "old_component_type_history_id")
})
public class ComponentSetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "component_set_id", nullable = false)
    private Long componentSetId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_type_history_id", nullable = false)
    private ComponentTypeHistory componentTypeHistory;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_name_set_history_id", nullable = false)
    private ComponentNameSetHistory componentNameSetHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_component_type_history_id")
    private ComponentTypeHistory oldComponentTypeHistory;

    @NotNull
    @Column(name = "value", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String value;

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