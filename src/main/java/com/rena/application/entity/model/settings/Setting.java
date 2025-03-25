package com.rena.application.entity.model.settings;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "setting")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "prev_shift", nullable = false)
    private Integer prevShift;

    @NotNull
    @Column(name = "next_boiler_number", nullable = false)
    private Integer nextBoilerNumber;

    @NotNull
    @Column(name = "amount_printed_barcode", nullable = false)
    private Integer amountPrintedBarcode;

    @NotNull
    @Column(name = "last_zeroing", nullable = false)
    private LocalDateTime lastZeroing;
}