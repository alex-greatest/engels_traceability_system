package com.rena.application.entity.model.settings.component.material;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "material", indexes = {
        @Index(name = "idx_material_value", columnList = "value"),
        @Index(name = "idx_material_material_type_id", columnList = "material_type_id")
})
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_type_id", nullable = false)
    private MaterialType materialType;
}