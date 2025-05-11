package com.rena.application.service.traceability.common.operation;

import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OperationTraceabilityService {
    private final OperationRepository operationRepository;
    private final PartLastRepository partLastRepository;

    public void createOperation(Boiler boiler,
                                Integer numberShift,
                                StationHistory station,
                                UserHistory userHistory,
                                Integer status) {
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
                                Integer status,
                                String ignoringError) {
        var operation = operationRepository.findLatestActiveByStationName(stationName,status)
                .orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        operation.setDateUpdate(LocalDateTime.now());
        operation.setStatus(status);
        operation.setIgnoringError(ignoringError);
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
