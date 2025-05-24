package com.rena.application.service.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.operation.OperationInterruptedRequest;
import com.rena.application.entity.dto.traceability.common.router.ErrorRoute;
import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.service.settings.shift.ShiftService;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
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
public class OperationRouterService {
    private final StationRouterService stationRouterService;
    private final BoilerRepository boilerRepository;
    private final StationHistoryRepository stationHistoryRepository;
    private final ComponentsPrepareOperationService componentsPrepareOperationService;
    private final UserHistoryRepository userHistoryRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final ShiftService shiftService;
    private final BoilerTraceabilityService boilerTraceabilityService;
    private final PartLastRepository partLastRepository;

    @Transactional
    public RpcBase getDataForStation(@Valid OperationStartRoute operationStartRoute) {
        var checkResult = stationRouterService.checkStations(operationStartRoute);
        if (checkResult.getIsOk()) {
            return createOperation(operationStartRoute);
        }
        var errorRoute = new ErrorRoute(checkResult.formatErrorStations(), operationStartRoute.getSerialNumber());
        errorRoute.setCorrelationId(operationStartRoute.getCorrelationId());
        return errorRoute;
    }

    public RpcBase createOperation(OperationStartRoute operationStartRoute) {
        var boiler = boilerRepository.findBySerialNumber(operationStartRoute.getSerialNumber()).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        var station = stationHistoryRepository.findByName(operationStartRoute.getStationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        return selectStationResponse(boiler, station);
    }

    private RpcBase selectStationResponse(Boiler boiler, StationHistory station) {
        var typeStation = station.getStationType().getName();
        return switch (typeStation) {
            case "Компоненты" -> componentsPrepareOperationService.createResponseOperationComponents(boiler, station);
            default -> throw new RecordNotFoundException("Неизвестный тип станции: " + station.getStationType());
        };
    }

    @Transactional
    public void interruptedOperation(@Valid OperationInterruptedRequest operationInterruptedRequest) {
        var admin = userHistoryRepository.findByCodeAndIsActive(operationInterruptedRequest.getAdminInterrupted(), true).
                orElseThrow(() -> new RecordNotFoundException("Администратор не найден"));
        operationTraceabilityService.updateOperation(
                operationInterruptedRequest.getStationName(),
                3,
                4,
                operationInterruptedRequest.getMessage(),
                false,
                admin);
        boilerTraceabilityService.updateBoiler(
                operationInterruptedRequest.getSerialNumber(),
                operationInterruptedRequest.getStationName(),
                4);
        partLastRepository.updatePart_idByStation(null, operationInterruptedRequest.getStationName());
    }
}
