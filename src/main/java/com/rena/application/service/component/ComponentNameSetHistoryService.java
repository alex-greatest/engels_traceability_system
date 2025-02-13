package com.rena.application.service.component;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.ComponentNameSetHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentNameSetHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ComponentNameSetHistoryService {
    private final ComponentNameSetHistoryRepository componentNameSetHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addComponentNameSetHistory(Long componentNameSetId, String oldNameNameSet, String nameNameSet, int typeOperation) {
        changeActiveStatusOld(componentNameSetId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        ComponentNameSetHistory componentNameSetHistory = new ComponentNameSetHistory();
        componentNameSetHistory.setComponentNameSetId(componentNameSetId);
        componentNameSetHistory.setName(nameNameSet);
        componentNameSetHistory.setTypeOperation(typeOperation);
        componentNameSetHistory.setOldName(oldNameNameSet != null && typeOperation == 2 ? oldNameNameSet : null);
        componentNameSetHistory.setIsActive(true);
        componentNameSetHistory.setUserId(userHistory.getUserId());
        componentNameSetHistory.setModifiedDate(LocalDateTime.now());
        componentNameSetHistoryRepository.save(componentNameSetHistory);
    }

    private void changeActiveStatusOld(Long componentNameSetId, int typeOperation)
    {
        if (typeOperation == 1 || componentNameSetId == null) {
            return;
        }
        componentNameSetHistoryRepository.findByComponentNameSetIdAndIsActive(componentNameSetId, true).ifPresent((c) -> {
            c.setIsActive(false);
            componentNameSetHistoryRepository.save(c);
        });
    }
}
