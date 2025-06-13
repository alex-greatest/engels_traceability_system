package com.rena.application.repository.traceability.station.materials;

import com.rena.application.entity.model.traceability.station.material.PackagingLabelHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagingLabelHistoryRepository extends JpaRepository<PackagingLabelHistory, Long> {
}