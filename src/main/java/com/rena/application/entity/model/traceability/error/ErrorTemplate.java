package com.rena.application.entity.model.traceability.error;

import com.rena.application.entity.model.traceability.common.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "error_template")
public class ErrorTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "error_step_id", nullable = false)
    private ErrorStep errorStep;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "error_message_id", nullable = false)
    private ErrorMessage errorMessage;
}