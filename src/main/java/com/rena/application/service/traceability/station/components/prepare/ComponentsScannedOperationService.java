package com.rena.application.service.traceability.station.components.prepare;

import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentTypeStation;
import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentsScannedOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.repository.settings.component.ComponentBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentsScannedOperationService {
    private final ComponentBindingRepository componentBindingRepository;

    public ComponentsScannedOperation getComponentsScanned(Boiler boiler, String stationName) {
        var boilerTypeId = boiler.getBoilerTypeCycle().getBoilerTypeId();
        var componentsType = componentBindingRepository.findByStation_Name(stationName, boilerTypeId).stream().
                map(componentType -> {
                    return new ComponentTypeStation(componentType.getName(), componentType.getCode(), null);
                }).toList();
        return new ComponentsScannedOperation(componentsType);
    }
}
