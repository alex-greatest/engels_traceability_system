package com.rena.application.entity.model.result.station;

import com.rena.application.entity.model.result.common.Boiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "serial_number_log_manual", indexes = {
        @Index(name = "idx_serial_number_log_manual", columnList = "boiler_serial_number")
})
public class SerialNumberLogManual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boiler_serial_number")
    private Boiler boiler;
}