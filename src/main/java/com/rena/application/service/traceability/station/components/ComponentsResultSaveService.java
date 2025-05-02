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
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.service.settings.shift.ShiftService;
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
    private final OperationRepository operationRepository;
    private final StationRepository stationRepository;
    private final PartLastRepository partLastRepository;
    private final ShiftService shiftService;

    @Transactional
    public Object saveResultsComponent(@Valid ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        var operation = operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(1, componentsOperationSaveResultRequest.stationName(),
                componentsOperationSaveResultRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var statusCommon = componentsOperationSaveResultRequest.status().equals("OK") ? 1 : 2;
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(statusCommon);
        var componentsResult = componentsOperationSaveResultRequest.componentsResultSave();
        saveComponentsResult(componentsResult, operation);
        var amountBoilerShift = statusCommon == 1 ?
                shiftService.updateShiftStation(componentsOperationSaveResultRequest.stationName(), 1) :
                shiftService.getAmountBoilerMade(componentsOperationSaveResultRequest.stationName());
        partLastRepository.updatePart_idByStation(null, componentsOperationSaveResultRequest.stationName());
        return null;
        //return new WpResponse(amountBoilerShift);
    }

    private void updateBoiler(Integer statusCommon) {
        /*var boiler = boilerRepository.findBySerialNumber(componentsResultRequest.serialNumber()).
                orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        var station = stationRepository.findByName(componentsResultRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(statusCommon);
        boiler.setLastStation(station);*/
    }

    private void saveComponentsResult(List<ComponentsResultSave> componentsResultSave, Operation operation) {
        componentsResultSave.forEach(componentResult -> {
            var component = new Component();
            component.setName(componentResult.componentType().name());
            component.setValue(componentResult.scannedValue());
            component.setOperation(operation);
            component.setStatus(componentResult.status().equals("OK") ? 1 : 2);
            componentRepository.save(component);
        });
    }

    @Transactional
    public void interruptedOperation(@Valid OperationInterruptedRequest operationInterruptedRequest) {
        var operation = operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(1, operationInterruptedRequest.stationName(),
                operationInterruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var boiler = boilerRepository.findBySerialNumber(operationInterruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        var station = stationRepository.findByName(operationInterruptedRequest.stationName()).
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
