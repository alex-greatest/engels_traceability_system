package com.rena.application.controller.result.websocket.station.wp;

import com.rena.application.entity.dto.result.common.ErrorRoute;
import com.rena.application.entity.dto.result.station.wp.components.ComponentsResultRequest;
import com.rena.application.entity.dto.result.station.wp.InterruptedRequest;
import com.rena.application.entity.dto.result.station.wp.components.ComponentRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerPrevStationEmpty;
import com.rena.application.exceptions.result.boiler.BoilerTraceabilityOK;
import com.rena.application.exceptions.result.boiler.BoilerTraceabilityPrevNOK;
import com.rena.application.service.result.station.wp.TraceabilityWpEnd;
import com.rena.application.service.result.station.wp.TraceabilityWpStart;
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
    private final TraceabilityWpStart traceabilityWpStart;
    private final TraceabilityWpEnd traceabilityWpEnd;

    @MessageMapping("/station/start/operation/request")
    public void getBoilerOrder(@Payload ComponentRequest componentRequest) {
        try {
            var boilerComponents = traceabilityWpStart.getComponentsBoiler(componentRequest);
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
            var wpResponse = traceabilityWpEnd.saveResultsComponent(componentsResultRequest);
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
            var boilerComponents = traceabilityWpStart.getLastOperation(nameStation);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/operation/get/last/response", nameStation), boilerComponents);
        } catch (Exception e) {
            log.error("Получение последний операции. Станция {}", nameStation, e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/operation/get/last/response", nameStation), "");
        }
    }

   @MessageMapping("/station/interrupted/operation/request")
    public void interruptedOperation(@Payload InterruptedRequest interruptedRequest) {
        try {
            traceabilityWpEnd.interruptedOperation(interruptedRequest);
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

