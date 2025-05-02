package com.rena.application.service.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.station.StationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class OperationRouterService {
    private final StationRouterService stationRouterService;
    private final StationRepository stationRepository;

    public void getDataForStation(@Valid OperationStartRoute operationStartRoute) {
        var checkResult = stationRouterService.checkStations(operationStartRoute);
        if (checkResult.isOk()) {
        }
        var station = stationRepository.findByName(operationStartRoute.getStationName())
                .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        selectStationResponse(operationStartRoute, station);
    }

    private RpcBase selectStationResponse(OperationStartRoute operationStartRoute, Station station) {
        switch (operationStartRoute.getStationName()) {
            case "Компоненты" -> ;
            default -> log.error("Неизвестный тип станции: {}", operationStartRoute.getStationName());
        }

    }
}
