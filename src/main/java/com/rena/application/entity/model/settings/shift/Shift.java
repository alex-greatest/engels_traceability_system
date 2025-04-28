package com.rena.application.entity.model.settings.shift;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "shift", uniqueConstraints = {
        @UniqueConstraint(name = "uc_shift_number", columnNames = {"number"})
})
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false, unique = true)
    private Integer number;

    @NotNull
    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;

    @NotNull
    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;
}