package com.rena.application.controller.result.websocket;

import com.rena.application.entity.dto.user.station.OperatorRequestAuthorization;
import com.rena.application.entity.dto.user.station.UserRequestAuthorization;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserAuthorization {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @MessageMapping("/operator/authorization/request")
    public void getOperatorAuthorization(@Payload OperatorRequestAuthorization operatorRequestAuthorization) {
        try {
            var operator = userService.getOperatorAuthorization(operatorRequestAuthorization);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response",
                    operatorRequestAuthorization.station()), operator);
        } catch (RecordNotFoundException e) {
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/errors",
                            operatorRequestAuthorization.station()),
                    "Оператор не найден");
        } catch (Exception e) {
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/errors",
                            operatorRequestAuthorization.station()),
                    "Неизвестная ошибка");
        }
    }

    @MessageMapping("/operator/authorization/logout/request")
    public void stationOperatorLogOut(@Payload OperatorRequestAuthorization userRequestAuthorization) {
        try {
            userService.stationLogout(userRequestAuthorization);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/response",
                    userRequestAuthorization.station()), "");
        } catch (RecordNotFoundException e) {
            log.error("Выход из аккаунта оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/errors",
                            userRequestAuthorization.station()),
                    e.getMessage());
        } catch (Exception e) {
            log.error("Выход из аккаунта оператор", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/errors",
                            userRequestAuthorization.station()),
                    "Неизвестная ошибка");
        }
    }

    @MessageMapping("/user/authorization/request")
    public void getUserAuthorization(@Payload UserRequestAuthorization userRequestAuthorization) {
        try {
            var user = userService.getUserAuthorization(userRequestAuthorization);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/response", userRequestAuthorization.station()), user);
        } catch (RecordNotFoundException e) {
            log.error("Авторизация админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/errors", userRequestAuthorization.station()),
                    e.getMessage());
        } catch (Exception e) {
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/errors", userRequestAuthorization.station()),
                    "Неизвестная ошибка");
        }
    }

    @MessageMapping("/user/authorization/logout/request")
    public void stationUserLogOut(@Payload UserRequestAuthorization userRequestAuthorization) {
        try {
            userService.stationLogout(userRequestAuthorization);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/logout/response", userRequestAuthorization.station()), "");
        } catch (RecordNotFoundException e) {
            log.error("Выход из аккаунта админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/logout/errors", userRequestAuthorization.station()),
                    e.getMessage());
        } catch (Exception e) {
            log.error("Выход из аккаунта оператор", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/authorization/logout/errors", userRequestAuthorization.station()),
                    "Неизвестная ошибка");
        }
    }
}