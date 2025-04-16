package com.rena.application.controller.result.websocket.station.wp.one;

import com.rena.application.entity.dto.traceability.station.wp.one.traceability.SerialLogManualRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.station.wp.one.traceability.SerialNumberLogManualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SerialNumberLogManualController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SerialNumberLogManualService serialNumberLogManualService;

    @MessageMapping("/boiler/wp1/get/barcode/log/request")
    public void getBarcodeLog(String serialNumber) {
        try {
            var barcode = serialNumberLogManualService.getBarcodeLog(serialNumber);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/log/response", "");
        } catch (RecordNotFoundException e) {
            log.error("Получение этикетки для ручной печати", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/log/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Получение этикетки для ручной печати", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/log/errors", "Неизвестная ошибка");
        }
    }

    @MessageMapping("/boiler/wp1/save/serial/log/request")
    public void saveSerial(@Payload SerialLogManualRequest serialLogManualRequest) {
        try {
            serialNumberLogManualService.addSerialNumberLogManual(serialLogManualRequest);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/serial/log/response", "");
        } catch (RecordNotFoundException e) {
            log.error("Сохранение лога после ручной печати", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/serial/log/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Сохранение лога после ручной печати", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/serial/log/errors", "Неизвестная ошибка");
        }
    }
}

