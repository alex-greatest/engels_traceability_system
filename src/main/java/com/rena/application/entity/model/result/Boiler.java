package com.rena.application.entity.model.result;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boiler", indexes = {
        @Index(name = "idx_boiler_boiler_type_id", columnList = "boiler_type_id"),
        @Index(name = "idx_boiler_date_create", columnList = "date_create"),
        @Index(name = "idx_boiler_date_update", columnList = "date_update"),
        @Index(name = "idx_boiler_user_id", columnList = "user_id")
})
public class Boiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "boiler_type_id", nullable = false)
    private Long boilerTypeId;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;
}