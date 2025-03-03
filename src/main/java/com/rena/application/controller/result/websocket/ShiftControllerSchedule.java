package com.rena.application.controller.result.websocket;

import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.service.shift.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShiftControllerSchedule {
    private final SettingRepository settingRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiftService shiftService;

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
            log.error("Обнуление предыдущей смены", e);
        }
    }

    @Async("schedulingTaskExecutor")
    @Scheduled(fixedDelay = 30000)
    public void resetShiftsStation() {
        try {
            shiftService.resetShiftsStation().ifPresent((shift) -> {
                messagingTemplate.convertAndSend("/message/current/shift", shift.getNumber());
            });
        } catch (Exception e) {
            log.error("Обнуление предыдущей смены", e);
        }
    }
}
