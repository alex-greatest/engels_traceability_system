package com.rena.application.service.traceability.station.component;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.station.component.ComponentsResultSave;
import com.rena.application.entity.dto.traceability.station.component.operation.ComponentsOperationSaveResultRequest;
import com.rena.application.entity.model.settings.component.ComponentValue;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.station.component.Component;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.component.ComponentTypeRepository;
import com.rena.application.repository.settings.component.ComponentValueRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.station.components.ComponentRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class ComponentsResultSaveService {
    private final ComponentTypeRepository componentTypeRepository;
    private final ComponentRepository componentRepository;
    private final ComponentValueRepository componentValueRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final PartLastRepository partLastRepository;
    private final BoilerTraceabilityService boilerTraceabilityService;
    private final MainInformationService mainInformationService;
    private final UserHistoryRepository userHistoryRepository;

    @Transactional
    public BoilerMadeInformation saveResultsComponent(@Valid ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        var admin = isRequiredAdmin(componentsOperationSaveResultRequest.getIsIgnoringError(),
                componentsOperationSaveResultRequest.getAdminIgnoringError());
        var operation = operationTraceabilityService.updateOperation(
                componentsOperationSaveResultRequest.getStationName(),
                3,
                1,
                null,
                componentsOperationSaveResultRequest.getIsIgnoringError(),
                admin);
        var componentsResult = componentsOperationSaveResultRequest.getComponentsResultSave();
        saveComponentsResult(componentsResult, operation);
        saveComponentValue(componentsOperationSaveResultRequest.getIsIgnoringError(), componentsResult, operation.getBoiler());
        var boiler = boilerTraceabilityService.updateBoiler(componentsOperationSaveResultRequest.getSerialNumber(),
                componentsOperationSaveResultRequest.getStationName(),
                1);
        partLastRepository.updatePart_idByStation(null, componentsOperationSaveResultRequest.getStationName());
        return mainInformationService.
                getBoilerMadeInfo(boiler.getBoilerOrder(), componentsOperationSaveResultRequest.getStationName());
    }

    private void saveComponentsResult(List<ComponentsResultSave> componentsResultSave, Operation operation) {
        componentsResultSave.forEach(componentResult -> saveComponent(componentResult, operation));
    }

    private void saveComponent(ComponentsResultSave componentResult, Operation operation) {
        var component = new Component();
        component.setName(componentResult.name());
        component.setValue(componentResult.scannedValue());
        component.setOperation(operation);
        component.setStatus(componentResult.status());
        component.setCode(componentResult.scannedCode());
        componentRepository.save(component);
    }

    private void saveComponentValue(Boolean isIgnoring, List<ComponentsResultSave> componentsResultSave, Boiler boiler) {
        if (!isIgnoring) {
            return;
        }
        componentsResultSave.stream().filter(componentResult -> componentResult.status().equals(2))
                .forEach(componentResult -> saveComponentValue(componentResult, boiler));

    }

    private void saveComponentValue(ComponentsResultSave componentResult, Boiler boiler) {
        var componentType = componentTypeRepository.findByCodeAndName(componentResult.scannedCode(), componentResult.name())
                .orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден: " + componentResult.name()));
        var value = new ComponentValue();
        value.setBoiler(boiler);
        value.setComponentType(componentType);
        value.setValue(componentResult.scannedValue());
        componentValueRepository.save(value);
    }

    private UserHistory isRequiredAdmin(Boolean isIgnoring, Integer code) {
        if (!isIgnoring) {
            return null;
        }
        return userHistoryRepository.findByCodeAndIsActive(code, true).
                orElseThrow(() -> new RecordNotFoundException("Администратор не найден"));
    }
}