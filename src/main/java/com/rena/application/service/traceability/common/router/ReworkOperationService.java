package com.rena.application.service.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReworkOperationService {
    private final OperationTraceabilityService operationTraceabilityService;
    private final StationHistoryRepository stationHistoryRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final BoilerTraceabilityService boilerTraceabilityService;

    @Transactional
    public void startReworkOperation(@Valid OperationStartRoute operationStartRoute) {
        var station = stationHistoryRepository.findByName("Доработка").
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var user = userHistoryRepository.
                findUserHistoryForActiveOperatorByStationName(operationStartRoute.getStationName()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        checkUser(user);
        var boiler = boilerTraceabilityService.updateBoiler(operationStartRoute.getSerialNumber(),
                station,
                1,
                operationStartRoute.getStationName());
        operationTraceabilityService.createOperation(boiler, station, user, 1);
    }

    private void checkUser(UserHistory user) {
        if (user.getRole().getName().equals("ROLE_Оператор")) {
            throw new RecordNotFoundException("У пользователя нет прав для выполнения этой операции");
        }
    }
}
