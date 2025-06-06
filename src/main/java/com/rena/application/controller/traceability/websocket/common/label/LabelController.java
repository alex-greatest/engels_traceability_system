package com.rena.application.controller.traceability.websocket.common.label;

import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.entity.dto.traceability.label.LabelStationNameData;
import com.rena.application.entity.dto.traceability.label.LabelValuesManualRequest;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.label.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LabelController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorHelper errorHelper;
    private final LabelService labelService;

    @MessageMapping("/station/label/last/request")
    public void getLastLabelStation(@Payload @Valid StationNameData stationNameData) {
        try {
            var labelLast = labelService.getLastLabel(stationNameData.getNameStation());
            labelLast.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/last/response", stationNameData.getNameStation()), labelLast);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Получение последний этикетки. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/last/response/error", stationNameData.getNameStation()), error);
        }
    }

    @MessageMapping("/station/label/list/request")
    public void getListLabels(@Payload @Valid StationNameData stationNameData) {
        try {
            var labels = labelService.getListLabels(stationNameData.getNameStation());
            labels.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/list/response",
                    stationNameData.getNameStation()), labels);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Получение списка типов этикеток. Станция {}", stationNameData.getNameStation(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/list/response/error",
                    stationNameData.getNameStation()), error);
        }
    }

    @MessageMapping("/station/label/variables/request")
    public void getListVariables(@Payload @Valid LabelStationNameData labelStationNameData) {
        try {
            var labels = labelService.getListLabelValue(labelStationNameData.getLabelName());
            labels.setCorrelationId(labelStationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/variables/response",
                    labelStationNameData.getStationName()), labels);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", labelStationNameData.getCorrelationId());
            log.error("Получение списка переменных для этикеток. Станция {}", labelStationNameData.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/variables/response/error",
                    labelStationNameData.getStationName()), error);
        }
    }

    @MessageMapping("/station/label/select/request")
    public void selectLabelType(@Payload LabelStationNameData labelStationNameData) {
        try {
            var labels = labelService.selectLabelType(labelStationNameData);
            labels.setCorrelationId(labelStationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/response",
                    labelStationNameData.getStationName()), labels);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), labelStationNameData.getCorrelationId());
            log.error("Выбор этикетки. Станция {}", labelStationNameData.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/response/error",
                    labelStationNameData.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", labelStationNameData.getCorrelationId());
            log.error("Выбор этикетки. Станция {}", labelStationNameData.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/response/error",
                    labelStationNameData.getStationName()), error);
        }
    }

    @MessageMapping("/station/label/select/manual/request")
    public void selectLabelTypeManual(@Payload LabelValuesManualRequest labelValuesManualRequest) {
        try {
            var labels = labelService.selectLabelTypeManual(labelValuesManualRequest);
            labels.setCorrelationId(labelValuesManualRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/manual/response",
                    labelValuesManualRequest.getStationName()), labels);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), labelValuesManualRequest.getCorrelationId());
            log.error("Выбор этикетки. Ручная печать. Станция {}", labelValuesManualRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/manual/response/error",
                    labelValuesManualRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", labelValuesManualRequest.getCorrelationId());
            log.error("Выбор этикетки. Ручная печать. Станция {}", labelValuesManualRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/label/select/manual/response/error",
                    labelValuesManualRequest.getStationName()), error);
        }
    }
}

