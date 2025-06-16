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
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.repository.traceability.station.components.ComponentRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.service.traceability.common.boiler.BoilerOrderCounterService;
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
    private final StationHistoryRepository stationHistoryRepository;
    private final BoilerOrderCounterService boilerOrderCounterService;

    @Transactional
    public BoilerMadeInformation saveResultsComponent(@Valid ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        var station = stationHistoryRepository.findByName(componentsOperationSaveResultRequest.getStationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
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
        var boiler = boilerTraceabilityService.updateBoiler(componentsOperationSaveResultRequest.getSerialNumber(), 1, station);
        boilerOrderCounterService.updateOrderCounter(boiler.getBoilerOrder(), station);
        partLastRepository.updatePart_idByStation(null, componentsOperationSaveResultRequest.getStationName());
        return mainInformationService.getBoilerMadeInfo(boiler.getBoilerOrder(),
                componentsOperationSaveResultRequest.getStationName(),
                false);
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
        var value = new ComponentValue();
        value.setBoiler(boiler);
        value.setName(componentResult.name());
        value.setCode(componentResult.scannedCode());
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