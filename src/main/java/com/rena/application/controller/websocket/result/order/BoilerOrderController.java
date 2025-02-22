package com.rena.application.controller.websocket.result.order;

import com.rena.application.entity.dto.result.order.Canban;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.result.order.BoilerOrderService;
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
    private final BoilerOrderService boilerOrderService;

    @MessageMapping("/boiler/order/get/request")
    public void getBoilerOrder(@Payload Canban canban) {
        try {
            var boilerOrder = boilerOrderService.addOrder(canban);
            messagingTemplate.convertAndSend("/message/boiler/order/get/response", boilerOrder);
        } catch (RecordNotFoundException e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/get/errors", e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/message/boiler/order/get/errors", "Неизвестная ошибка");
        }
    }
}

