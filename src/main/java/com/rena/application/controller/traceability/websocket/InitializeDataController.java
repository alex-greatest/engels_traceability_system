package com.rena.application.controller.traceability.websocket;

import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
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
public class InitializeDataController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorHelper errorHelper;
    private final MainInformationService mainInformationService;

    @MessageMapping("/initialize/data/request")
    public void getOperatorAuthorization(@Payload StationNameData stationNameData) {
        try {
            var mainDataStation = mainInformationService.createInitializeData(stationNameData.getNameStation());
            mainDataStation.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/data/response",
                    stationNameData.getNameStation()), mainDataStation);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/data/response/error", stationNameData.getNameStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/data/response/error", stationNameData.getNameStation()), error);
        }
    }
}