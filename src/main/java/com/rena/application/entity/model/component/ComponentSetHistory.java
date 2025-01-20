package com.rena.application.entity.model.component;

import com.rena.application.entity.model.user.User;
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
        @Index(name = "idx_component_set_history", columnList = "component_history_id"),
        @Index(name = "idx_component_name_set_history", columnList = "component_name_set_history_id"),
        @Index(name = "idx_component_is_active_value", columnList = "is_active, value")
})
public class ComponentSetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_history_id")
    private ComponentHistory componentHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_name_set_history_id")
    private ComponentNameSetHistory componentNameSetHistory;

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

    @Column(name = "is_active", nullable = false)
    @JdbcTypeCode(SqlTypes.BIT)
    private Boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_code", nullable = false)
    private User user;
}