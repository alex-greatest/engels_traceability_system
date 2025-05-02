package com.rena.application.controller.traceability.websocket.station.components;

import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationSaveResultRequest;
import com.rena.application.entity.dto.traceability.common.operation.OperationInterruptedRequest;
import com.rena.application.entity.dto.traceability.common.router.OperationStartRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.station.components.ComponentsOperationEndService;
import com.rena.application.service.traceability.station.components.ComponentsOperationStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ComponentsOperationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ComponentsOperationStartService componentsOperationStartService;
    private final ComponentsOperationEndService componentsOperationEndService;

    @MessageMapping("/station/start/operation/request")
    public void getBoilerOrder(@Payload OperationStartRequest operationStartRequest) {
        try {
            var componentsResponse = componentsOperationStartService.startOperation(operationStartRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/response",
                    operationStartRequest.stationName()),
                    componentsResponse);
        } catch (Exception e) {
            log.error("Получение компонентво. Станция {}", operationStartRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/errors", operationStartRequest.stationName()), "Неизвестная ошибка");
        }
    }

    @MessageMapping("/station/end/operation/request")
    public void saveComponents(@Payload ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        try {
            var wpResponse = componentsOperationEndService.saveResultsComponent(componentsOperationSaveResultRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/response",
                            componentsOperationSaveResultRequest.stationName()), wpResponse);
        } catch (RecordNotFoundException e) {
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsOperationSaveResultRequest.stationName()), e.getMessage());
        } catch (Exception e) {
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsOperationSaveResultRequest.stationName()), "Неизвестная ошибка");
        }
    }

   @MessageMapping("/station/interrupted/operation/request")
    public void interruptedOperation(@Payload OperationInterruptedRequest operationInterruptedRequest) {
        try {
            componentsOperationEndService.interruptedOperation(operationInterruptedRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            operationInterruptedRequest.stationName()),
                    "");
        } catch (RecordNotFoundException e) {
            log.error("Прерывание операции. Станция {}", operationInterruptedRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            operationInterruptedRequest.stationName()), e.getMessage());
        }
        catch (Exception e) {
            log.error("Прерывание операции. Станция {}", operationInterruptedRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            operationInterruptedRequest.stationName()),
                    "Неизвестная ошибка");
        }
    }
}

