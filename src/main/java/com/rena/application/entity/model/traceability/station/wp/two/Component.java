package com.rena.application.entity.model.traceability.station.wp.two;

import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component", indexes = {
        @Index(name = "idx_component_name", columnList = "name"),
        @Index(name = "idx_component_status_id", columnList = "status_id"),
        @Index(name = "idx_component_operation_id", columnList = "operation_id")
})
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private Operation operation;
}