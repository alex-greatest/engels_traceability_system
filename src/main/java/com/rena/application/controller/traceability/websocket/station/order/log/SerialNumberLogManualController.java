package com.rena.application.controller.traceability.websocket.station.order.log;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.order.log.SerialLogManualRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.station.order.log.SerialNumberLogManualService;
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
    private final ErrorHelper errorHelper;

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

    @MessageMapping("/boiler/station/save/serial/log/request")
    public void saveSerial(@Payload SerialLogManualRequest serialLogManualRequest) {
        try {
            serialNumberLogManualService.addSerialNumberLogManual(serialLogManualRequest);
            var rpcBase = new RpcBase();
            rpcBase.setCorrelationId(serialLogManualRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/save/serial/log/response",
                    serialLogManualRequest.getStationName()), rpcBase);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), serialLogManualRequest.getCorrelationId());
            log.error("Сохранения лога распечатанных этикеток. Станция {}", serialLogManualRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/save/serial/log/response/error",
                    serialLogManualRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", serialLogManualRequest.getCorrelationId());
            log.error("Сохранения лога распечатанных этикеток. Станция {}", serialLogManualRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/save/serial/log/response/error",
                    serialLogManualRequest.getStationName()), error);
        }
    }
}

