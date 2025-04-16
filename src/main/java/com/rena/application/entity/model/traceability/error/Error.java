package com.rena.application.entity.model.traceability.error;

import com.rena.application.entity.model.user.UserHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "error", indexes = {
        @Index(name = "idx_error_date_create", columnList = "date_create"),
        @Index(name = "idx_error_user_history_id", columnList = "user_history_id"),
        @Index(name = "idx_error_serial_number", columnList = "serial_number")
})
public class Error {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "error_template_id", nullable = false)
    private ErrorTemplate errorTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_history_id", nullable = false)
    private UserHistory userHistory;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}