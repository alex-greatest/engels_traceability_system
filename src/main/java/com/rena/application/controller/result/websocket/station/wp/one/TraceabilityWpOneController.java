package com.rena.application.controller.result.websocket.station.wp.one;

import com.rena.application.entity.dto.result.station.wp.one.WpOneRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.result.station.wp.one.TraceabilityOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TraceabilityWpOneController {
    private final SimpMessagingTemplate messagingTemplate;
    private final TraceabilityOneService traceabilityOneService;

    @MessageMapping("/boiler/wp1/print/request")
    public void getBoilerOrder(@Payload WpOneRequest wpOneRequest) {
        try {
            var wpOneResponse = traceabilityOneService.generateBarcodeData(wpOneRequest);
            messagingTemplate.convertAndSend("/message/boiler/wp1/print/response", wpOneResponse);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/print/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/print/errors", "Неизвестная ошибка");
        }
    }
}

