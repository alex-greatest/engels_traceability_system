package com.rena.application.controller.traceability.websocket;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.router.OperationStartRoute;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.common.router.ReworkOperationService;
import com.rena.application.service.traceability.common.router.OperationRouterService;
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
    private final OperationRouterService operationRouterService;
    private final ReworkOperationService reworkOperationService;

    @MessageMapping("/station/operation/start/request")
    public void operationStart(@Payload OperationStartRoute operationStartRoute) {
        try {
            var checkResult = operationRouterService.getDataForStation(operationStartRoute);
            checkResult.setCorrelationId(operationStartRoute.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/station/operation/response",
                    operationStartRoute.getStationName()), checkResult);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operationStartRoute.getCorrelationId());
            log.error("Получение данных для теста. Станция {}", operationStartRoute.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/station/operation/response/error",
                    operationStartRoute.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operationStartRoute.getCorrelationId());
            log.error("Получение данных для теста. Станция {}", operationStartRoute.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/station/operation/response/error",
                    operationStartRoute.getStationName()), error);
        }
    }

    @MessageMapping("/station/rework/request")
    public void operationRework(@Payload OperationStartRoute operationStartRoute) {
        try {
            reworkOperationService.startReworkOperation(operationStartRoute);
            var response = new RpcBase();
            response.setCorrelationId(operationStartRoute.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/station/rework/response",
                    operationStartRoute.getStationName()), response);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operationStartRoute.getCorrelationId());
            log.error("Добавление станции доработки. Станция {}", operationStartRoute.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/station/rework/response/error",
                    operationStartRoute.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operationStartRoute.getCorrelationId());
            log.error("Добавление станции доработки. Станция {}", operationStartRoute.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/station/rework/response/error",
                    operationStartRoute.getStationName()), error);
        }
    }
}