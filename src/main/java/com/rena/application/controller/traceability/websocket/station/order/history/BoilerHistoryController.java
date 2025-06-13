package com.rena.application.controller.traceability.websocket.station.order.history;

import com.rena.application.entity.dto.settings.Paging;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryRequest;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryResponse;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.station.order.history.BoilerOrderHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoilerHistoryController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorHelper errorHelper;
    private final BoilerOrderHistoryService boilerOrderHistoryService;

    @MessageMapping("/boiler/list/history/request")
    public void getBoilers(BoilerHistoryRequest boilerHistoryRequest) {
        try {
            Page<BoilerHistoryResponse> boilers = boilerOrderHistoryService.getBoilers(boilerHistoryRequest);
            var boilersPaging = new Paging<>(boilers.getTotalElements(), boilers.getContent());
            boilersPaging.setCorrelationId(boilerHistoryRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/list/history/response",
                    boilerHistoryRequest.getStationName()), boilersPaging);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), boilerHistoryRequest.getCorrelationId());
            log.error("Получение истории котлов, по id заказа. Станция {}", boilerHistoryRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/list/history/response/error",
                    boilerHistoryRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", boilerHistoryRequest.getCorrelationId());
            log.error("Получение истории котлов, по id заказа. Станция {}", boilerHistoryRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/%s/boiler/list/history/response/error",
                    boilerHistoryRequest.getStationName()), error);
        }
    }
}