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
@Table(name = "component_value_history", indexes = {
        @Index(name = "idx_component_value_history_component_id", columnList = "component_id"),
        @Index(name = "idx_component_value_history_user_code", columnList = "user_code"),
        @Index(name = "idx_component_value_history_is_acitve", columnList = "is_active")
})
public class ComponentValueHistory {
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