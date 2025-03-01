package com.rena.application.entity.model.result.station.wp.two;

import com.rena.application.entity.model.result.common.Status;
import com.rena.application.entity.model.result.station.wp.one.Boiler;
import com.rena.application.entity.model.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component", indexes = {
        @Index(name = "idx_component_boiler_serial_number", columnList = "boiler_serial_number"),
        @Index(name = "idx_component_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_component_name", columnList = "name"),
        @Index(name = "idx_component_status_id", columnList = "status_id")
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

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boiler_serial_number", nullable = false)
    private Boiler boiler;
}