package com.rena.application.controller.traceability.websocket.station.order.history;

import com.rena.application.entity.dto.settings.Paging;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryRequest;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryResponse;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.station.order.history.BoilerHistoryWpOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerHistoryWpOneController {
    private final SimpMessagingTemplate messagingTemplate;
    private final BoilerHistoryWpOneService boilerHistoryWpOneService;

    @MessageMapping("/boiler/list/get_info/request")
    public void getBoilers(BoilerHistoryRequest boilerHistoryRequest) {
        try {
            Page<BoilerHistoryResponse> boilers = boilerHistoryWpOneService.getBoilers(boilerHistoryRequest);
            var boilersPaging = new Paging<>(boilers.getTotalElements(), boilers.getContent());
            messagingTemplate.convertAndSend(boilerHistoryRequest.destinationResponse(), boilersPaging);
        } catch (RecordNotFoundException e) {
            log.error("Boilers", e);
            messagingTemplate.convertAndSend(boilerHistoryRequest.destinationResponseError(), e.getMessage());
        } catch (Exception e) {
            log.error("Boilers", e);
            messagingTemplate.convertAndSend(boilerHistoryRequest.destinationResponseError(), "Неизвестная ошибка");
        }
    }
}