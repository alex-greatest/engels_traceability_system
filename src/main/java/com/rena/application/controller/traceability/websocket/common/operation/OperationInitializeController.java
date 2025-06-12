package com.rena.application.controller.traceability.websocket.common.operation;

import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.service.traceability.common.initialize.OperationOrderInitializeService;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.common.initialize.OperationComponentsMaterialsInitializeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OperationInitializeController {
    private final SimpMessagingTemplate messagingTemplate;
    private final OperationComponentsMaterialsInitializeService operationComponentsMaterialsInitializeService;
    private final OperationOrderInitializeService operationOrderInitializeService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/station/boiler/order/initialize/request")
    public void getLastBoilerOrder(@Payload @Valid StationNameData stationNameData) {
        try {
            var boilerOrder = operationOrderInitializeService.getLastBoilerOrder(stationNameData.getNameStation());
            boilerOrder.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/boiler/order/initialize/response",
                    stationNameData.getNameStation()), boilerOrder);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Получение последний операции. Канбан. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/boiler/order/initialize/response/error",
                    stationNameData.getNameStation()), error);
        }
    }

    @MessageMapping("/station/components/initialize/request")
    public void getLastOperationComponents(@Payload @Valid StationNameData stationNameData) {
        try {
            var response = operationComponentsMaterialsInitializeService.getLastOperation(stationNameData.getNameStation());
            response.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response",
                    stationNameData.getNameStation()), response);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Получение последний операции. Компоненты или материалы. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response/error",
                    stationNameData.getNameStation()), error);
        }
    }
}

