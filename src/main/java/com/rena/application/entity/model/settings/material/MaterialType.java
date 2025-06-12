package com.rena.application.entity.model.settings.material;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "material_type", uniqueConstraints = {
        @UniqueConstraint(name = "uc_material_type_code", columnNames = {"code"})
})
public class MaterialType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false, unique = true, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String code;

    @Column(name = "length_code", nullable = false)
    private Integer lengthCode;

    @Version
    @Column(name = "version")
    private Integer version;
}