package com.rena.application.entity.model.traceability.label;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "label_value", uniqueConstraints = {
        @UniqueConstraint(name = "uc_label_value_label_type_id_variable", columnNames = {"label_type_id", "variable", "value"})
})
public class LabelValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "label_type_id", nullable = false)
    private LabelType labelType;

    @Column(name = "variable", nullable = false)
    private String variable;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "is_active")
    private Boolean isActive;
}