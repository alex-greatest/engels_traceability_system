package com.rena.application.controller.traceability.websocket;

import com.rena.application.entity.dto.traceability.common.exchange.ErrorResponse;
import com.rena.application.entity.dto.traceability.common.exchange.InitializeData;
import com.rena.application.service.traceability.station.initialize.OperationInitializeService;
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
    public void getLastOperationComponents(@Payload InitializeData initializeData) {
        try {
            var response = operationInitializeService.getLastOperationComponents(initializeData.getNameStation());
            response.setCorrelationId(initializeData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response",
                    initializeData.getNameStation()), response);
        } catch (Exception e) {
            var nameStation = initializeData.getNameStation();
            var error = new ErrorResponse("Ошибка при получении данных для инициалзиации");
            error.setCorrelationId(initializeData.getCorrelationId());
            log.error("Получение последний операции. Станция {}", nameStation, e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/initialize/response/error", nameStation), error);
        }
    }
}

