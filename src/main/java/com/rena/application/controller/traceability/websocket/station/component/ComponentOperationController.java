package com.rena.application.controller.traceability.websocket.station.component;

import com.rena.application.entity.dto.traceability.station.component.result.ComponentResultRequest;
import com.rena.application.entity.dto.traceability.station.component.operation.ComponentsOperationSaveResultRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.station.component.ComponentResultSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.rena.application.service.traceability.station.component.ComponentsResultSaveService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ComponentOperationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ComponentsResultSaveService componentsResultSaveService;
    private final ComponentResultSaveService componentResultSaveService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/components/component/save/request")
    public void saveComponent(@Payload ComponentResultRequest componentResultRequest) {
        try {
            var componentResultResponse = componentResultSaveService.saveResultsComponent(componentResultRequest);
            componentResultResponse.setCorrelationId(componentResultRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/component/save/response",
                    componentResultRequest.getStationName()), componentResultResponse);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), componentResultRequest.getCorrelationId());
            log.error("Привязка компонента. Станция {}", componentResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/component/save/response/error",
                    componentResultRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", componentResultRequest.getCorrelationId());
            log.error("Привязка компонента. Станция {}", componentResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/component/save/response/error",
                    componentResultRequest.getStationName()), error);
        }
    }

    @MessageMapping("/components/result/operation/save/request")
    public void saveComponents(@Payload ComponentsOperationSaveResultRequest componentsOperationSaveResultRequest) {
        try {
            var boilerMadeInformation = componentsResultSaveService.saveResultsComponent(componentsOperationSaveResultRequest);
            boilerMadeInformation.setCorrelationId(componentsOperationSaveResultRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/save/operation/response",
                            componentsOperationSaveResultRequest.getStationName()), boilerMadeInformation);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), componentsOperationSaveResultRequest.getCorrelationId());
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/save/operation/response/error",
                    componentsOperationSaveResultRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", componentsOperationSaveResultRequest.getCorrelationId());
            log.error("Сохрание компонентов. Станция {}", componentsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/components/save/operation/response/error",
                    componentsOperationSaveResultRequest.getStationName()), error);
        }
    }
}

