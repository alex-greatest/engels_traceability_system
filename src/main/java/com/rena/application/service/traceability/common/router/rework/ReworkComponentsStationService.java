package com.rena.application.service.traceability.common.router.rework;

import com.rena.application.repository.settings.component.ComponentValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReworkComponentsStationService {
    private final ComponentValueRepository componentValueRepository;

    public void startReworkComponentsStation(String serialNumber) {
        componentValueRepository.deleteByBoiler(serialNumber);
    }
}
