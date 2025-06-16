package com.rena.application.controller.traceability.websocket.station.order;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerGetRequest;
import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeSaveRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
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
    private final ErrorHelper errorHelper;

    @MessageMapping("/boiler/station/get/barcode/request")
    public void getBoilerSerialNumber(@Payload BarcodeBoilerGetRequest barcodeBoilerOrderPrintRequest) {
        try {
            var barcodes = boilerOrderStartOperationService.generateBarcodeData(barcodeBoilerOrderPrintRequest);
            barcodes.setCorrelationId(barcodeBoilerOrderPrintRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/get/barcode/response",
                            barcodeBoilerOrderPrintRequest.getStation()), barcodes);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), barcodeBoilerOrderPrintRequest.getCorrelationId());
            log.error("Генерация нового баркода. Станция {}", barcodeBoilerOrderPrintRequest.getStation(), e);
            messagingTemplate.convertAndSend("/message/%s/boiler/station/get/barcode/response/error", error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", barcodeBoilerOrderPrintRequest.getCorrelationId());
            log.error("Генерация нового баркода. Станция {}", barcodeBoilerOrderPrintRequest.getStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/add/response/error",
                    barcodeBoilerOrderPrintRequest.getStation()), error);
        }
    }

    @MessageMapping("/boiler/station/save/barcode/request")
    public void saveSerialNumber(@Payload BarcodeSaveRequest barcodeSaveRequest) {
        try {
            var barcodeSaveResponse = boilerOrderEndOperationService.saveBarcodes(barcodeSaveRequest);
            barcodeSaveResponse.setCorrelationId(barcodeSaveRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/save/barcode/response",
                            barcodeSaveRequest.getStation()),
                    barcodeSaveResponse);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), barcodeSaveRequest.getCorrelationId());
            log.error("Результат сохранения баркода. Станция {}", barcodeSaveRequest.getStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/get/barcode/response/error",
                    barcodeSaveRequest.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", barcodeSaveRequest.getCorrelationId());
            log.error("Результат сохранения баркода. Станция {}", barcodeSaveRequest.getStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/station/get/barcode/response/error",
                    barcodeSaveRequest.getStation()), error);
        }
    }
}

