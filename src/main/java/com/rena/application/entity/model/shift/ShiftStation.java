package com.rena.application.entity.model.shift;

import com.rena.application.entity.model.traceability.common.Station;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shift_station")
public class ShiftStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @NotNull
    @Column(name = "amount_boiler_made", nullable = false)
    private Integer amountBoilerMade;
}