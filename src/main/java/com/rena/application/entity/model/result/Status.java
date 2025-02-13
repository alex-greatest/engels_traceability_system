package com.rena.application.entity.model.result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "status", uniqueConstraints = {
        @UniqueConstraint(name = "uc_status_name", columnNames = {"name"})
})
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 10)
    private String name;
}