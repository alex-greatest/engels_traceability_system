package com.rena.application.controller.websocket.result.order;

import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerOrderControllerStation {
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiftService userService;

    @MessageMapping("/boiler/order/get/request")
    public void getBoilerOrder(String stationName) {
        try {
            var shifts = userService.getAllShifts();
            messagingTemplate.convertAndSend("/boiler/order/get/response");
        } catch (RecordNotFoundException e) {
            messagingTemplate.convertAndSend("/boiler/order/get/errors",
                    e.getMessage());
        } catch (Exception e) {
            log.error("Канбан карта", e);
            messagingTemplate.convertAndSend("/boiler/order/get/errors", "Неизвестная ошибка");
        }
    }
}

