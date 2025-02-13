package com.rena.application.service.component;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.ComponentSetHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentSetHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ComponentSetHistoryService {
    private final ComponentSetHistoryRepository componentSetHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addComponentHistory(Long componentSetId, Long componentTypeId, Long componentNameSetId,
                                    Long oldComponentTypeId, String value, int typeOperation) {
        changeActiveStatusOld(componentSetId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        ComponentSetHistory componentSetHistory = new ComponentSetHistory();
        componentSetHistory.setComponentNameSetId(componentNameSetId);
        componentSetHistory.setOldComponentTypeId(oldComponentTypeId != null && typeOperation == 2
                ? oldComponentTypeId : null);
        componentSetHistory.setComponentSetId(componentSetId);
        componentSetHistory.setTypeOperation(typeOperation);
        componentSetHistory.setComponentTypeId(componentTypeId);
        componentSetHistory.setValue(value);
        componentSetHistory.setIsActive(true);
        componentSetHistory.setUserHistoryId(userHistory.getUserId());
        componentSetHistory.setModifiedDate(LocalDateTime.now());
        componentSetHistoryRepository.save(componentSetHistory);
    }

    private void changeActiveStatusOld(Long componentSetId, int typeOperation)
    {
        if (typeOperation == 1 || componentSetId == null) {
            return;
        }
        componentSetHistoryRepository.findByIsActiveAndComponentSetId(true, componentSetId).ifPresent((c) -> {
            c.setIsActive(false);
            componentSetHistoryRepository.save(c);
        });
    }
}
