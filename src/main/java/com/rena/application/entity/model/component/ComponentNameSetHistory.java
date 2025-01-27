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
@Table(name = "component_name_set_history", indexes = {
        @Index(name = "idx_component_name_set_history", columnList = "is_active, name"),
        @Index(name = "idx_component_name_set_history_user_history_id", columnList = "user_history_id")
})
public class ComponentNameSetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @Column(name = "old_name", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String oldName;

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