package com.rena.application.controller.traceability.websocket.station.components;

import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationSaveResultRequest;
import com.rena.application.entity.dto.traceability.common.operation.OperationInterruptedRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.station.components.ComponentsResultSaveService;
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
    private final ComponentsResultSaveService componentsResultSaveService;

    @MessageMapping("/components/result/operation/save/request")
    public void saveComponents(@Payload ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        try {
            var wpResponse = componentsResultSaveService.saveResultsComponent(componentsOperationSaveResultRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/response",
                            componentsOperationSaveResultRequest.getStationName()), wpResponse);
        } catch (RecordNotFoundException e) {
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsOperationSaveResultRequest.getStationName()), e.getMessage());
        } catch (Exception e) {
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsOperationSaveResultRequest.getStationName()), "Неизвестная ошибка");
        }
    }
}

