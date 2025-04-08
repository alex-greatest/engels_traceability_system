package com.rena.application.entity.model.result.station;

import com.rena.application.entity.model.result.common.Boiler;
import com.rena.application.entity.model.user.UserHistory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_serial_number", nullable = false)
    private Boiler boiler;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "shift_number", nullable = false)
    private Integer shiftNumber;

    @Column(name = "amount", nullable = false)
    private Integer amount;
}