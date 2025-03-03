package com.rena.application.controller.result.websocket;

import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.shift.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShiftControllerStation {
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiftService shiftService;

    @MessageMapping("/shift/get_info/request")
    public void getShift(String stationName) {
        try {
            var shift = shiftService.getCurrentShift();
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/response", stationName), shift.getNumber());
        } catch (RecordNotFoundException e) {
            log.error("Смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/errors", stationName),
                    e.getMessage());
        } catch (Exception e) {
            log.error("Смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/errors", stationName),
                    "Неизвестная ошибка");
        }
    }

    @MessageMapping("/shift/made/boiler/get_info/request")
    public void getAmountMadeBoiler(String stationName) {
        try {
            var amountBoilerMade = shiftService.getAmountBoilerMade(stationName);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/amount/made/boiler/get_info/response", stationName),
                    amountBoilerMade);
        } catch (RecordNotFoundException e) {
            log.error("Смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/amount/made/boiler/get_info/errors", stationName),
                    e.getMessage());
        } catch (Exception e) {
            log.error("Смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/amount/made/boiler/get_info/errors", stationName),
                    "Неизвестная ошибка");
        }
    }
}

