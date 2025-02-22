package com.rena.application.entity.model.result.error;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "error", indexes = {
        @Index(name = "idx_error_date_create", columnList = "date_create"),
        @Index(name = "idx_error_user_id", columnList = "user_id"),
        @Index(name = "idx_error_boiler_id", columnList = "boiler_id")
})
public class Error {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "boiler_id")
    private Long boilerId;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "error_template_id", nullable = false)
    private ErrorTemplate errorTemplate;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}