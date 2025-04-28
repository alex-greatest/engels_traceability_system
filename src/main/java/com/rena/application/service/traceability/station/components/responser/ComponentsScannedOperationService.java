package com.rena.application.service.traceability.station.components.responser;

import com.rena.application.config.mapper.component.set.ComponentSetMapper;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsScannedOperation;
import com.rena.application.entity.model.settings.type.BoilerType;
import com.rena.application.entity.model.settings.component.ComponentType;
import com.rena.application.entity.model.settings.component.set.ComponentSet;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.settings.type.BoilerTypeRepository;
import com.rena.application.repository.settings.component.set.ComponentSetRepository;
import com.rena.application.repository.settings.component.set.ComponentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComponentsScannedOperationService {
    private final ComponentSetMapper componentSetMapper;
    private final ComponentSetRepository componentSetRepository;
    private final BoilerTypeRepository boilerTypeRepository;
    private final ComponentTypeRepository componentTypeRepository;

    public ComponentsScannedOperation getComponentsScanned(Boiler boiler, String stationName) {
        var boilerType = boilerTypeRepository.findBoilerById(boiler.getBoilerTypeCycle().getBoilerTypeId()).
                orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
        var componentsType = getComponentsType(stationName, boilerType);
        var componentsSet = getComponentsSet(boilerType, componentsType, stationName);
        var components = componentSetMapper.toComponentSetOperation(componentsSet);
        var componentsNameType = componentsType.stream().map(ComponentType::getName).toList();
        return new ComponentsScannedOperation(components, componentsNameType);
    }

    private List<ComponentType> getComponentsType(String stationName, BoilerType boilerType) {
        var boilerTypeId = boilerType.getComponentNameSet().getId();
        return componentTypeRepository.findComponentTypesByStationNameAndNameSetId(stationName, boilerTypeId);
    }

    private List<ComponentSet> getComponentsSet(BoilerType boilerType,
                                                List<ComponentType> componentTypes,
                                                String stationName) {
        var boilerTypeId = boilerType.getComponentNameSet().getId();
        var componentsTypeFromBinding = componentTypes.stream().map(ComponentType::getId).toList();
        return componentSetRepository.findAllComponentsByBindingsIdOrdered(
                boilerTypeId,
                componentsTypeFromBinding,
                stationName);
    }
}
