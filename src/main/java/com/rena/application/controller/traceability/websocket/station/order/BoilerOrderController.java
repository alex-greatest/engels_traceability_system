package com.rena.application.controller.traceability.websocket.station.order;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.entity.dto.traceability.station.order.canban.Canban;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.station.order.BoilerOrderManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerOrderController {
    private final SimpMessagingTemplate messagingTemplate;
    private final BoilerOrderManageService boilerOrderManageService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/boiler/order/add/request")
    public void addBoilerOrder(@Payload Canban canban) {
        try {
            var boilerOrder = boilerOrderManageService.getOrder(canban);
            boilerOrder.setCorrelationId(canban.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/add/response", canban.getStationName()), boilerOrder);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), canban.getCorrelationId());
            log.error("Канбан карта. Станция {}", canban.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/add/response/error", canban.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", canban.getCorrelationId());
            log.error("Канбан карта. Станция {}", canban.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/add/response/error", canban.getStationName()), error);
        }
    }

    @MessageMapping("/boiler/order/cycle/interrupted/request")
    public void getAmountPrinterBoilerOrder(@Payload StationNameData stationNameData) {
        try {
            boilerOrderManageService.interruptedOperation(stationNameData.getNameStation());
            var rpcBase = new RpcBase();
            rpcBase.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/cycle/interrupted/response",
                    stationNameData.getNameStation()), rpcBase);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), stationNameData.getCorrelationId());
            log.error("Прерывание операции. Заказы. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/cycle/interrupted/response/error",
                    stationNameData.getNameStation()), error);
        }
        catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Прерывание операции. Заказы. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/order/cycle/interrupted/response/error",
                    stationNameData.getNameStation()), error);
        }
    }
}

