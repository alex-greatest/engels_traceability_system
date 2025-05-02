package com.rena.application.service.traceability.station.components.prepare;

import com.rena.application.config.mapper.component.set.ComponentSetMapper;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsScannedOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.settings.type.BoilerTypeRepository;
import com.rena.application.repository.settings.component.set.ComponentSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentsScannedOperationService {
    private final ComponentSetMapper componentSetMapper;
    private final ComponentSetRepository componentSetRepository;
    private final BoilerTypeRepository boilerTypeRepository;

    public ComponentsScannedOperation getComponentsScanned(Boiler boiler, String stationName) {
        var boilerTypeId = boiler.getBoilerTypeCycle().getBoilerTypeId();
        checkBoilerExists(boilerTypeId);
        var componentsSet = componentSetRepository.findAllComponentsByStationOrdered(boilerTypeId, stationName);
        var components = componentSetMapper.toComponentSetOperation(componentsSet);
        var componentsNameType = componentSetRepository.findDistinctComponentTypeNamesByStationAndNameSet(boilerTypeId, stationName);
        return new ComponentsScannedOperation(components, componentsNameType);
    }

    public void checkBoilerExists(Long boilerId) {
        if (!boilerTypeRepository.existsById(boilerId)) {
            throw new BoilerTypeNotFoundException("Тип котла не найден");
        }
    }
}
