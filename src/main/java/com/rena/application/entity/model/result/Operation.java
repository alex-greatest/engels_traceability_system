package com.rena.application.entity.model.result;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "operation", indexes = {
        @Index(name = "idx_operation_date_create", columnList = "date_create"),
        @Index(name = "idx_operation_user_id", columnList = "user_id")
})
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;
}