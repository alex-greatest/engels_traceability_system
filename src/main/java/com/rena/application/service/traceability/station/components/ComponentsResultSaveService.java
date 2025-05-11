package com.rena.application.service.traceability.station.components;

import com.rena.application.entity.dto.traceability.station.components.ComponentsResultSave;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationSaveResultRequest;
import com.rena.application.entity.dto.traceability.common.operation.OperationInterruptedRequest;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.station.component.Component;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.ComponentRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class ComponentsResultSaveService {
    private final BoilerRepository boilerRepository;
    private final ComponentRepository componentRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final StationHistoryRepository stationHistoryRepository;
    private final PartLastRepository partLastRepository;
    private final BoilerTraceabilityService boilerTraceabilityService;

    @Transactional
    public Object saveResultsComponent(@Valid ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        var operation = operationTraceabilityService.updateOperation(componentsOperationSaveResultRequest.getStationName(),
                componentsOperationSaveResultRequest.getStatus(), componentsOperationSaveResultRequest.getIgnoringMessage());
        var componentsResult = componentsOperationSaveResultRequest.getComponentsResultSave();
        saveComponentsResult(componentsResult, operation);
        boilerTraceabilityService.updateBoiler(componentsOperationSaveResultRequest.getSerialNumber(),
                componentsOperationSaveResultRequest.getStationName(),
                componentsOperationSaveResultRequest.getStatus());
        partLastRepository.updatePart_idByStation(null, componentsOperationSaveResultRequest.getStationName());
        return null;
    }

    private void saveComponentsResult(List<ComponentsResultSave> componentsResultSave, Operation operation) {
        componentsResultSave.forEach(componentResult -> {
            var component = new Component();
            component.setName(componentResult.componentType());
            component.setValue(componentResult.scannedValue());
            component.setOperation(operation);
            component.setStatus(componentResult.status());
            componentRepository.save(component);
        });
    }

    @Transactional
    public void interruptedOperation(@Valid OperationInterruptedRequest operationInterruptedRequest) {
        var operation = operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(1, operationInterruptedRequest.stationName(),
                operationInterruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var boiler = boilerRepository.findBySerialNumber(operationInterruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        var station = stationHistoryRepository.findByName(operationInterruptedRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(4);
        boiler.setLastStation(station);
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(4);
        operation.setReasonForStopping(operationInterruptedRequest.message());
        partLastRepository.updatePart_idByStation(null, operationInterruptedRequest.stationName());
    }
}
