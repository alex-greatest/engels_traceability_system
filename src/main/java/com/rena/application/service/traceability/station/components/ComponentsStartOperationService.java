package com.rena.application.service.traceability.station.components;

import com.rena.application.entity.dto.traceability.common.router.OperationStartRequest;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.boiler.*;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.service.settings.shift.ShiftService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import com.rena.application.service.traceability.station.components.prepare.ComponentsPrepareOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class ComponentsStartOperationService {
    private final BoilerRepository boilerRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final ComponentsPrepareOperationService componentsPrepareOperationService;
    private final StationHistoryRepository stationHistoryRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ShiftService shiftService;

    @Transactional
    public ComponentsOperationStartResponse startOperation(@Valid OperationStartRequest operationStartRequest) {
        var stationName = operationStartRequest.stationName();
        var serialNumber = operationStartRequest.serialNumber();
        var numberShift = shiftService.getCurrentShift().getNumber();
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new BoilerNotFoundException("Котёл не найден"));
        var station = stationHistoryRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var user = userHistoryRepository.findUserHistoryForActiveOperatorByStationName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        operationTraceabilityService.createOperation(boiler, numberShift, station, user, 3);
        return componentsPrepareOperationService.createResponseOperationComponents(boiler, stationName);
    }
}
