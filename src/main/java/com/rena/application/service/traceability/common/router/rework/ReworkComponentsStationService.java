package com.rena.application.service.traceability.common.router.rework;

import com.rena.application.repository.settings.component.ComponentValueRepository;
import com.rena.application.repository.settings.material.MaterialValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReworkComponentsStationService {
    private final ComponentValueRepository componentValueRepository;
    private final MaterialValueRepository materialValueRepository;

    public void startReworkComponentsStation(String serialNumber) {
        componentValueRepository.deleteByBoiler(serialNumber);
    }

    public void startReworkMaterialStation(String serialNumber) {
        materialValueRepository.deleteByBoiler(serialNumber);
    }
}
