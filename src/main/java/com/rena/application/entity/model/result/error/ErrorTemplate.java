package com.rena.application.entity.model.result.error;

import com.rena.application.entity.model.result.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "error_template", uniqueConstraints = {
        @UniqueConstraint(name = "uc_error_template_name", columnNames = {"name"})
})
public class ErrorTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 500, unique = true)
    private String message;

    @Column(name = "step", nullable = false, length = 100)
    private String step;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "error_step_id", nullable = false)
    private ErrorStep errorStep;
}