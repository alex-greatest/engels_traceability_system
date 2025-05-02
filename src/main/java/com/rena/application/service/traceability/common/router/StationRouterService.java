package com.rena.application.service.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.entity.dto.traceability.common.router.StationRouteCheckResult;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StationRouterService {
    private final OperationRepository operationRepository;
    private final BoilerRepository boilerRepository; // Добавлено
    private final StationRepository stationRepository; // Добавлено
    private static final String REWORK_STATION_NAME = "Доработка"; // Имя станции доработки
    private static final int OPERATION_STATUS_OK = 1; // Статус успешной операции

    public StationRouteCheckResult checkStations(OperationStartRoute operationStartRoute) {
        String serialNumber = operationStartRoute.getSerialNumber();
        String stationName = operationStartRoute.getStationName();
        checkBoiler(serialNumber);
        checkStation(stationName);
        return operationRepository.checkStationRoute(
                serialNumber, stationName, REWORK_STATION_NAME, OPERATION_STATUS_OK);
    }

    private void checkBoiler(String serialNumber) {
        if (!boilerRepository.existsById(serialNumber)) {
            throw new RecordNotFoundException("Котел с серийным номером " + serialNumber + " не найден");
        }
    }

    private void checkStation(String stationName) {
        if (!stationRepository.existsByName(stationName)) {
            throw new RecordNotFoundException("Станция с именем " + stationName + " не найдена");
        }
    }
}
