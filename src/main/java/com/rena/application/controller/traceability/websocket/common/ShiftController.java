package com.rena.application.controller.traceability.websocket.common;

import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.entity.dto.traceability.common.initialize.ShiftNumber;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.service.settings.shift.ShiftService;
import com.rena.application.service.traceability.helper.ErrorHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ShiftController {
    private final SettingRepository settingRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiftService shiftService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/initialize/station/shift/request")
    public void initializeData(@Payload @Valid StationNameData stationNameData) {
        try {
            var shift = shiftService.getCurrentShiftSynchronized();
            var shiftNumber = new ShiftNumber(shift.getNumber());
            shiftNumber.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/station/shift/response",
                    stationNameData.getNameStation()), shiftNumber);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции. Получение номера смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/station/shift/response/error",
                    stationNameData.getNameStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции. Получение номера смены", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/initialize/station/shift/response/error",
                    stationNameData.getNameStation()), error);
        }
    }

    @Async("schedulingTaskExecutor")
    @Scheduled(fixedDelay = 60 * 2000)
    public void zeroing() {
        try {
            var setting = settingRepository.findById(1L).orElseThrow(() -> new RecordNotFoundException("Настройки не найдены"));
            if (LocalDateTime.now().getMonth() != setting.getLastZeroing().getMonth()) {
                setting.setLastZeroing(LocalDateTime.now());
                setting.setNextBoilerNumber(1);
                settingRepository.save(setting);
            }
        } catch (Exception e) {
            log.error("Обнуление порядкового номера котла", e);
        }
    }

    @Async("schedulingTaskExecutor")
    @Scheduled(fixedDelay = 30000)
    public void resetShiftsStation() {
        try {
            shiftService.resetShiftsStation().ifPresent((shift) ->
                    messagingTemplate.convertAndSend("/message/current/shift", shift.getNumber()));
        } catch (Exception e) {
            log.error("Обнуление предыдущей смены", e);
        }
    }
}
