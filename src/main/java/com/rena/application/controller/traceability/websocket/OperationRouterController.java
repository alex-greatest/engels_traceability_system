package com.rena.application.controller.traceability.websocket;

import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.service.traceability.common.router.StationRouterService;
import com.rena.application.service.traceability.helper.ErrorHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OperationRouterController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorHelper errorHelper;
    private final StationRouterService stationRouterService;
    private final StationRepository stationRepository;

    @MessageMapping("/station/operation/start")
    public void getOperatorAuthorization(@Payload OperationStartRoute operationStartRoute) {
        try {
            var checkResult = stationRouterService.checkStations(operationStartRoute);
            var station = stationRepository.findByName(operationStartRoute.getStationName())
                    .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
            selectStationResponse(operationStartRoute, station);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operatorRequestAuthorization.getCorrelationId());
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operatorRequestAuthorization.getCorrelationId());
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        }
    }

    private void selectStationResponse(OperationStartRoute operationStartRoute, Station station) {
        switch (operationStartRoute.getStationName()) {
            case "Компоненты" -> messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response",
                    operationStartRoute.getStation()), station);
            default -> log.warn("Unknown station: {}", operationStartRoute.getStationName());
        }

    }
}