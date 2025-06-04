package com.rena.application.entity.model.traceability.station.order;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "boiler_label_count")
public class BoilerLabelCount {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Boiler boiler;

    @Column(name = "amount_print_type")
    private Integer amountPrintType;
}