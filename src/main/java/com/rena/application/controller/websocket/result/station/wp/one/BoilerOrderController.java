package com.rena.application.controller.websocket.result.station.wp.one;

import com.rena.application.entity.dto.result.station.wp.one.order.Canban;
import com.rena.application.entity.dto.result.station.wp.one.order.CanbanUniqueCode;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.result.station.wp.one.order.BoilerOrderWpOneService;
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
    private final BoilerOrderWpOneService boilerOrderWpOneService;

    @MessageMapping("/boiler/order/add/request")
    public void addBoilerOrder(@Payload CanbanUniqueCode canbanUniqueCode) {
        try {
            var boilerOrder = boilerOrderWpOneService.addOrder(canbanUniqueCode);
            messagingTemplate.convertAndSend("/message/boiler/order/add/response", boilerOrder);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/add/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/add/errors", "Неизвестная ошибка");
        }
    }

    @MessageMapping("/boiler/order/get/request")
    public void updateBoilerOrder(@Payload Canban canban) {
        try {
            var boilerOrder = boilerOrderWpOneService.updateOrder(canban);
            messagingTemplate.convertAndSend("/message/boiler/order/get/response", boilerOrder);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/get/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/get/errors", "Неизвестная ошибка");
        }
    }

    @MessageMapping("/boiler/order/last/get/request")
    public void getLastBoilerOrder() {
        try {
            var boilerOrder = boilerOrderWpOneService.getLastBoilerOrder();
            messagingTemplate.convertAndSend("/message/boiler/order/last/get/response", boilerOrder);
        } catch (Exception e) {
            log.error("Получение последнего заказа", e);
            messagingTemplate.convertAndSend("/message/boiler/order/last/get/errors", "");
        }
    }
}

