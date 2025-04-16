package com.rena.application.controller.result.websocket.station.wp;

import com.rena.application.entity.dto.traceability.common.ErrorRoute;
import com.rena.application.entity.dto.traceability.station.wp.components.ComponentsResultRequest;
import com.rena.application.entity.dto.traceability.station.wp.InterruptedRequest;
import com.rena.application.entity.dto.traceability.station.wp.components.ComponentRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerPrevStationEmpty;
import com.rena.application.exceptions.traceability.boiler.BoilerTraceabilityOK;
import com.rena.application.exceptions.traceability.boiler.BoilerTraceabilityPrevNOK;
import com.rena.application.service.traceability.station.wp.TraceabilityWpEndService;
import com.rena.application.service.traceability.station.wp.TraceabilityWpStartService;
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
public class TraceabilityWpController {
    private final SimpMessagingTemplate messagingTemplate;
    private final TraceabilityWpStartService traceabilityWpStartService;
    private final TraceabilityWpEndService traceabilityWpEndService;

    @MessageMapping("/station/start/operation/request")
    public void getBoilerOrder(@Payload ComponentRequest componentRequest) {
        try {
            var boilerComponents = traceabilityWpStartService.getComponentsBoiler(componentRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/operation/response", componentRequest.stationName()), boilerComponents);
        } catch (BoilerTraceabilityOK e) {
            log.error("Получение компонентов. Ошибка маршрута. Станция {}", componentRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/errors", componentRequest.stationName()),
                    new ErrorRoute(e.getMessage(), componentRequest.serialNumber(), true));
        } catch (BoilerTraceabilityPrevNOK | BoilerPrevStationEmpty e) {
            log.error("Получение компонентов. Ошибка маршрута. Станция {}", componentRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/errors", componentRequest.stationName()),
                    new ErrorRoute(e.getMessage(), componentRequest.serialNumber(), !componentRequest.stationName().equals("wp2")));
        } catch (RecordNotFoundException e) {
            log.error("Получение компонентво. Станция {}", componentRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/errors", componentRequest.stationName()),
                    new ErrorRoute(e.getMessage(), "", false));
        } catch (Exception e) {
            log.error("Получение компонентво. Станция {}", componentRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/start/operation/errors", componentRequest.stationName()), "Неизвестная ошибка");
        }
    }

    @MessageMapping("/station/end/operation/request")
    public void saveComponents(@Payload ComponentsResultRequest componentsResultRequest) {
        try {
            var wpResponse = traceabilityWpEndService.saveResultsComponent(componentsResultRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/response",
                            componentsResultRequest.stationName()), wpResponse);
        } catch (RecordNotFoundException e) {
            log.error("Сохрание компонентов. Станция {}", componentsResultRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsResultRequest.stationName()), e.getMessage());
        } catch (Exception e) {
            log.error("Сохрание компонентов. Станция {}", componentsResultRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/end/operation/errors",
                    componentsResultRequest.stationName()), "Неизвестная ошибка");
        }
    }

    @MessageMapping("/station/start/operation/get/last/request")
    public void getLastOperation(@NotBlank String nameStation) {
        try {
            var boilerComponents = traceabilityWpStartService.getLastOperation(nameStation);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/operation/get/last/response", nameStation), boilerComponents);
        } catch (Exception e) {
            log.error("Получение последний операции. Станция {}", nameStation, e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/operation/get/last/response", nameStation), "");
        }
    }

   @MessageMapping("/station/interrupted/operation/request")
    public void interruptedOperation(@Payload InterruptedRequest interruptedRequest) {
        try {
            traceabilityWpEndService.interruptedOperation(interruptedRequest);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            interruptedRequest.stationName()),
                    "");
        } catch (RecordNotFoundException e) {
            log.error("Прерывание операции. Станция {}", interruptedRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            interruptedRequest.stationName()), e.getMessage());
        }
        catch (Exception e) {
            log.error("Прерывание операции. Станция {}", interruptedRequest.stationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/interrupted/operation/response",
                            interruptedRequest.stationName()),
                    "Неизвестная ошибка");
        }
    }
}

