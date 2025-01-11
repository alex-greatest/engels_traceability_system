package com.rena.application.entity.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "user_", indexes = {
        @Index(name = "idx_user__role_id", columnList = "role_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_user__username", columnNames = {"username"})
})
public class User {
    @Id
    @Column(name = "code", unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Integer code;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", length = 30)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}