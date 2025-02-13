package com.rena.application.controller.websocket;

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
public class ShiftControllerStation {
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiftService userService;

    @MessageMapping("/shift/get_info/request")
    public void getUserInfo(String stationName) {
        try {
            var shifts = userService.getAllShifts();
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/response", stationName), shifts);
        } catch (RecordNotFoundException e) {
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/errors", stationName),
                    "Оператор не найден");
        } catch (Exception e) {
            log.error("Смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/shift/get_info/errors", stationName), "Неизвестная ошибка");
        }
    }
}

