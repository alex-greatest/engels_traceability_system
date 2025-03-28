package com.rena.application.controller.result.websocket.station.wp.one;

import com.rena.application.entity.dto.result.station.wp.one.BarcodeGetOneWpRequest;
import com.rena.application.entity.dto.result.station.wp.one.BarcodeSaveOneWpRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.result.station.wp.one.TraceabilityWpOneEndService;
import com.rena.application.service.result.station.wp.one.TraceabilityWpOneStartService;
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
    private final TraceabilityWpOneStartService traceabilityWpOneStartService;
    private final TraceabilityWpOneEndService traceabilityWpOneEndService;

    @MessageMapping("/boiler/wp1/get/barcode/request")
    public void getBoilerSerialNumber(@Payload BarcodeGetOneWpRequest barcodeGetOneWpRequest) {
        try {
            var barcodes = traceabilityWpOneStartService.generateBarcodeData(barcodeGetOneWpRequest);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/response", barcodes);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/get/barcode/errors", "Неизвестная ошибка");
        }
    }

    @MessageMapping("/boiler/wp1/save/barcode/request")
    public void saveSerialNumber(@Payload BarcodeSaveOneWpRequest barcodeSaveOneWpRequest) {
        try {
            var barcodeSaveResponse = traceabilityWpOneEndService.saveBarcodes(barcodeSaveOneWpRequest);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/barcode/response", barcodeSaveResponse);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/barcode/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/wp1/save/barcode/errors", "Неизвестная ошибка");
        }
    }
}

