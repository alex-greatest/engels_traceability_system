package com.rena.application.controller.websocket.result.station.wp.one;

import com.rena.application.entity.dto.Paging;
import com.rena.application.entity.dto.result.station.wp.one.boiler.BoilerRequestWpOne;
import com.rena.application.entity.dto.result.station.wp.one.boiler.BoilerResponseWpOne;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.result.station.wp.one.BoilerWpOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerWpOneController {
    private final SimpMessagingTemplate messagingTemplate;
    private final BoilerWpOneService boilerWpOneService;

    @MessageMapping("/boiler/list/get_info/request")
    public void getBoilers(BoilerRequestWpOne boilerRequestWpOne) {
        try {
            Page<BoilerResponseWpOne> boilers = boilerWpOneService.getBoilers(boilerRequestWpOne);
            var boilersPaging = new Paging<>(boilers.getTotalElements(), boilers.getContent());
            messagingTemplate.convertAndSend(boilerRequestWpOne.destinationResponse(), boilersPaging);
        } catch (RecordNotFoundException e) {
            log.error("Boilers", e);
            messagingTemplate.convertAndSend(boilerRequestWpOne.destinationResponseError(), e.getMessage());
        } catch (Exception e) {
            log.error("Boilers", e);
            messagingTemplate.convertAndSend(boilerRequestWpOne.destinationResponseError(), "Неизвестная ошибка");
        }
    }
}