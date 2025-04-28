package com.rena.application.service.traceability.station.components;

import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartRequest;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.exceptions.traceability.boiler.*;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import com.rena.application.service.traceability.station.components.responser.ComponentsResponserOperationService;
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
public class ComponentsOperationStartService {
    private final BoilerRepository boilerRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final ComponentsResponserOperationService componentsResponserOperationService;

    @Transactional
    public ComponentsOperationStartResponse startOperation(@Valid ComponentsOperationStartRequest componentsOperationStartRequest) {
        var stationName = componentsOperationStartRequest.stationName();
        var serialNumber = componentsOperationStartRequest.serialNumber();
        var numberShift = componentsOperationStartRequest.numberShift();
        var userCode = componentsOperationStartRequest.userCode();
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new BoilerNotFoundException("Котёл не найден"));
        operationTraceabilityService.createOperation(boiler, numberShift, stationName, userCode, 3);
        return componentsResponserOperationService.createResponseOperationComponents(boiler, stationName);
    }
}
