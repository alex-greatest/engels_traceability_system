package com.rena.application.repository.traceability.station.materials;

import com.rena.application.entity.model.traceability.station.material.PackagingLabel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagingLabelRepository extends JpaRepository<PackagingLabel, Long> {
}