package com.rena.application.service.traceability.station.component.prepare;

import com.rena.application.entity.dto.traceability.station.component.test.ComponentTypeStation;
import com.rena.application.entity.dto.traceability.station.component.test.ComponentsScannedOperation;
import com.rena.application.entity.model.settings.component.ComponentType;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.repository.settings.component.ComponentBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentsScannedOperationService {
    private final ComponentBindingRepository componentBindingRepository;

    public ComponentsScannedOperation getComponentsScanned(Boiler boiler, String stationName) {
        var boilerTypeId = boiler.getBoilerTypeCycle().getBoilerTypeId();
        var componentBindings = componentBindingRepository.findByStation_Name(stationName, boilerTypeId);
        var componentsType = componentBindings.stream()
                .collect(Collectors.groupingBy(ComponentType::getName,
                        Collectors.mapping(ComponentType::getCode, Collectors.toList())))
                .entrySet().stream()
                .map(entry -> new ComponentTypeStation(entry.getKey(), entry.getValue(), null))
                .toList();
        return new ComponentsScannedOperation(componentsType);
    }
}
