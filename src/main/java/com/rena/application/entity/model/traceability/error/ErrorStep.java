package com.rena.application.entity.model.traceability.error;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "error_step", uniqueConstraints = {
        @UniqueConstraint(name = "uc_error_step_name", columnNames = {"name"})
})
public class ErrorStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}