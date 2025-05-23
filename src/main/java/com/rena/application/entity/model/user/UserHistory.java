package com.rena.application.entity.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_history", indexes = {
        @Index(name = "idx_user_history_role_id", columnList = "role_id"),
        @Index(name = "idx_user_history_code", columnList = "code"),
        @Index(name = "idx_user_history_is_active", columnList = "is_active"),
        @Index(name = "idx_user_history_user_id", columnList = "user_id"),
        @Index(name = "idx_user_history_type_operation", columnList = "type_operation")
})
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "code", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Integer code;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String username;

    @Column(name = "old_code")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Integer oldCode;

    @Size(max = 50)
    @Column(name = "old_username", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String oldUsername;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false, length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @Size(max = 50)
    @NotNull
    @Column(name = "username_changed", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String usernameChanged;

    @NotNull
    @Column(name = "code_changed", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Integer codeChanged;

    @NotNull
    @Column(name = "type_operation", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer typeOperation;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @JdbcTypeCode(SqlTypes.BIT)
    private Boolean isActive = false;
}