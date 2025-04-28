package com.rena.application.controller.traceability.websocket.station.order;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerOrderPrintRequest;
import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerOrderSavePrintRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.station.order.operation.BoilerOrderEndOperationService;
import com.rena.application.service.traceability.station.order.operation.BoilerOrderStartOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerOrderOperationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final BoilerOrderStartOperationService boilerOrderStartOperationService;
    private final BoilerOrderEndOperationService boilerOrderEndOperationService;

    @MessageMapping("/boiler/wp1/get/barcode/request")
    public void getBoilerSerialNumber(@Payload BarcodeBoilerOrderPrintRequest barcodeBoilerOrderPrintRequest) {
        try {
            var barcodes = boilerOrderStartOperationService.generateBarcodeData(barcodeBoilerOrderPrintRequest);
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
    public void saveSerialNumber(@Payload BarcodeBoilerOrderSavePrintRequest barcodeBoilerOrderSavePrintRequest) {
        try {
            var barcodeSaveResponse = boilerOrderEndOperationService.saveBarcodes(barcodeBoilerOrderSavePrintRequest);
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

