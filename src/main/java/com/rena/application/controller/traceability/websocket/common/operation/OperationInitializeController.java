package com.rena.application.controller.traceability.websocket.common.operation;

import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.common.initialize.OperationInitializeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    private final OperationInitializeService operationInitializeService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/boiler/order/last/get/request")
    public void getLastBoilerOrder(@NotBlank String nameStation) {
        try {
            //var boilerOrder = boilerOrderWpOneService.getLastBoilerOrder();
            //messagingTemplate.convertAndSend("/message/boiler/order/last/get/response", boilerOrder);
        } catch (Exception e) {
            log.error("Получение последнего заказа", e);
            messagingTemplate.convertAndSend("/message/boiler/order/last/get/errors", "");
        }
    }

    @MessageMapping("/station/components/initialize/request")
    public void getLastOperationComponents(@Payload @Valid StationNameData stationNameData) {
        try {
            var response = operationInitializeService.getLastOperationComponents(stationNameData.getNameStation());
            response.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response",
                    stationNameData.getNameStation()), response);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), stationNameData.getCorrelationId());
            log.error("Получение последний операции. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response/error",
                    stationNameData.getNameStation()), error);
        }
    }
}

