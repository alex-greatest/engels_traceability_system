package com.rena.application.service.traceability.common.router.rework;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentsScannedOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.service.traceability.station.components.prepare.ComponentsPrepareOperationService;
import com.rena.application.service.traceability.station.components.prepare.ComponentsScannedOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Validated
public class CheckerReworkStationService {
    private final StationRepository stationRepository;
    private final ReworkComponentsStationService reworkComponentsStationService;
    private final ComponentsPrepareOperationService componentsPrepareOperationService;
    private final ComponentsScannedOperationService componentsScannedOperationService;

    public void checkStationRework(String stationName, String serialNumber) {
        var station = stationRepository.findByName(stationName)
                .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var typeStation = station.getStationType().getName();
        switch (typeStation) {
            case "Компоненты" -> reworkComponentsStationService.startReworkComponentsStation(serialNumber);
            default -> {
            }
        }
    }

    public RpcBase startOperation(Boiler boiler, StationHistory station) {
        var typeStation = station.getStationType().getName();
        return switch (typeStation) {
            case "Компоненты" -> componentsPrepareOperationService.createResponseOperationComponents(boiler, station);
            default -> throw new RecordNotFoundException("Неизвестный тип станции: " + station.getStationType().getName());
        };
    }
}
