package com.rena.application.controller.websocket;

import com.rena.application.config.mapper.UserMapper;
import com.rena.application.entity.model.user.UserCodeWebsocket;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.user.UserRepository;
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
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    @MessageMapping("/user/get_info/request")
    public void getUserInfo(@Payload UserCodeWebsocket userCodeWebsocket) {
        try {
            var user = userService.getUser(userCodeWebsocket.code());
            messagingTemplate.convertAndSend(String.format("/message/%s/user/get_info/response", userCodeWebsocket.station()), user);
        } catch (RecordNotFoundException e) {
            messagingTemplate.convertAndSend(String.format("/message/%s/user/get_info/errors", userCodeWebsocket.station()),
                    "Оператор не найден");
        } catch (Exception e) {
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/user/get_info/errors", userCodeWebsocket.station()),
                    "Неизвестная ошибка");
        }
    }
}

