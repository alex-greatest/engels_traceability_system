package com.rena.application.service.traceability.station.wp;

import com.rena.application.entity.dto.traceability.station.wp.WpResponse;
import com.rena.application.entity.dto.traceability.station.wp.components.ComponentsResult;
import com.rena.application.entity.dto.traceability.station.wp.components.ComponentsResultRequest;
import com.rena.application.entity.dto.traceability.station.wp.InterruptedRequest;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.Status;
import com.rena.application.entity.model.traceability.station.wp.two.Component;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.*;
import com.rena.application.repository.traceability.station.wp.ComponentRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.service.shift.ShiftService;
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
public class TraceabilityWpEndService {
    private final BoilerRepository boilerRepository;
    private final ComponentRepository componentRepository;
    private final OperationRepository operationRepository;
    private final StatusRepository statusRepository;
    private final StationRepository stationRepository;
    private final PartLastRepository partLastRepository;
    private final ShiftService shiftService;

    @Transactional
    public WpResponse saveResultsComponent(@Valid ComponentsResultRequest componentsResultRequest) {
        var operation = operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(3L, componentsResultRequest.stationName(),
                componentsResultRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var boiler = boilerRepository.findBySerialNumber(componentsResultRequest.serialNumber()).
                orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        var statusOK = statusRepository.getReferenceById(1L);
        var statusNOK = statusRepository.getReferenceById(2L);
        var station = stationRepository.findByName(componentsResultRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var statusCommon = componentsResultRequest.status().equals("OK") ? statusOK : statusNOK;
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(statusCommon);
        boiler.setLastStation(station);
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(statusCommon);
        var componentsResult = componentsResultRequest.componentsResult();
        saveComponentsResult(componentsResult, operation, statusOK, statusNOK);
        var amountBoilerShift = statusCommon.getName().equals("OK") ?
                shiftService.updateShiftStation(componentsResultRequest.stationName(), 1) :
                shiftService.getAmountBoilerMade(componentsResultRequest.stationName());
        partLastRepository.updatePart_idByStation(null, componentsResultRequest.stationName());
        return new WpResponse(amountBoilerShift);
    }

    private void saveComponentsResult(List<ComponentsResult> componentsResult, Operation operation,
                                      Status statusOK, Status statusNOK) {
        componentsResult.forEach(componentResult -> {
            var component = new Component();
            component.setName(componentResult.componentType().name());
            component.setValue(componentResult.scannedValue());
            component.setOperation(operation);
            component.setStatus(componentResult.status().equals("OK") ? statusOK : statusNOK);
            componentRepository.save(component);
        });
    }

    @Transactional
    public void interruptedOperation(@Valid InterruptedRequest interruptedRequest) {
        var operation = operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(3L, interruptedRequest.stationName(),
                interruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var boiler = boilerRepository.findBySerialNumber(interruptedRequest.serialNumber()).orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        var status = statusRepository.getReferenceById(4L);
        var station = stationRepository.findByName(interruptedRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(status);
        boiler.setLastStation(station);
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(status);
        operation.setReasonForStopping(interruptedRequest.message());
        partLastRepository.updatePart_idByStation(null, interruptedRequest.stationName());
    }
}
