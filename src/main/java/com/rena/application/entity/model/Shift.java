package com.rena.application.entity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "shift")
public class Shift {
    @Id
    @Column(name = "number", nullable = false)
    private Long number;

    @NotNull
    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;

    @NotNull
    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;
}