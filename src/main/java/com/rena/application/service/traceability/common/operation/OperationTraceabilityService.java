package com.rena.application.service.traceability.common.operation;

import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.service.settings.shift.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OperationTraceabilityService {
    private final OperationRepository operationRepository;
    private final PartLastRepository partLastRepository;
    private final ShiftService shiftService;
    private final UserHistoryRepository userHistoryRepository;

    public void createOperation(Boiler boiler, StationHistory station, Integer status) {
        var user = userHistoryRepository.
                findUserHistoryForActiveOperatorByStationName(station.getName()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        createOperation(boiler, station, user, status);
    }

    public void createOperation(Boiler boiler,
                                StationHistory station,
                                UserHistory userHistory,
                                Integer status) {
        var numberShift = shiftService.getCurrentShiftStation().getNumber();
        Operation operation = new Operation();
        operation.setDateCreate(LocalDateTime.now());
        operation.setBoiler(boiler);
        operation.setNumberShift(numberShift);
        operation.setStationHistory(station);
        operation.setUserHistory(userHistory);
        operation.setStatus(status);
        operationRepository.save(operation);
        createLastPart(station.getName(), operation.getId());
    }

    public Operation updateOperation(String stationName,
                                     Integer oldStatus,
                                     Integer newStatus,
                                     String reasonForStopping,
                                     Boolean isIgnoringError,
                                     UserHistory adminInterrupted) {
        var operation = operationRepository.findLatestActiveByStationName(stationName,oldStatus)
                .orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(newStatus);
        operation.setIgnoring_error(isIgnoringError);
        operation.setReasonForStopping(reasonForStopping);
        operation.setAdminInterrupted(adminInterrupted);
        return operationRepository.save(operation);
    }

    public void createLastPart(String stationName, Long operationId) {
        if (stationName.equals("Доработка")) {
            return;
        }
        var partLast = partLastRepository.findByStation_Name(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        partLast.setPart_id(operationId.toString());
        partLastRepository.save(partLast);
    }
}
