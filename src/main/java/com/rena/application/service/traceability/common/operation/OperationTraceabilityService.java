package com.rena.application.service.traceability.common.operation;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.Operation;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.OperationRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OperationTraceabilityService {
    private final OperationRepository operationRepository;
    private final StationRepository stationRepository;
    private final PartLastRepository partLastRepository;
    private final UserHistoryRepository userHistoryRepository;

    public void createOperation(Boiler boiler, Integer numberShift, String stationName, Integer userCode, Integer status) {
        var user = userHistoryRepository.findByCodeAndIsActive(userCode, true).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        var station = stationRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        Operation operation = new Operation();
        operation.setDateCreate(LocalDateTime.now());
        operation.setBoiler(boiler);
        operation.setNumberShift(numberShift);
        operation.setStation(station);
        operation.setUserHistory(user);
        operation.setStatus(status);
        operation.setIsLast(true);
        operationRepository.save(operation);
        var partLast = partLastRepository.findByStation_Name(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        partLast.setPart_id(operation.getId().toString());
    }
}
