package com.rena.application.service.component;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.ComponentNameSetHistory;
import com.rena.application.entity.model.component.ComponentSetHistory;
import com.rena.application.entity.model.component.ComponentTypeHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentNameSetHistoryRepository;
import com.rena.application.repository.component.ComponentSetHistoryRepository;
import com.rena.application.repository.component.ComponentTypeHistoryRepository;
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
    private final ComponentNameSetHistoryRepository componentNameSetHistoryRepository;
    private final ComponentTypeHistoryRepository componentTypeHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addComponentHistory(Long componentSetId, String oldNameType,
                                    String componentNameSet, String componentTypeName, String value,
                                    boolean isActive, int typeOperation) {
        changeActiveStatusOld(componentSetId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        ComponentNameSetHistory componentNameSetHistory =
                componentNameSetHistoryRepository.findByNameAndIsActive(componentNameSet, true).
                        orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Набор компонентов не найден"));
        ComponentTypeHistory oldComponentTypeHistory = componentTypeHistoryRepository.findByNameAndIsActive(oldNameType, true).
                orElse(null);
        ComponentTypeHistory componentTypeHistory = componentTypeHistoryRepository.findByNameAndIsActive(componentTypeName, true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Тип компонента не найден"));
        ComponentSetHistory componentSetHistory = new ComponentSetHistory();
        componentSetHistory.setComponentNameSetHistory(componentNameSetHistory);
        componentSetHistory.setOldComponentTypeHistory(oldComponentTypeHistory != null && typeOperation == 2
                ? oldComponentTypeHistory : null);
        componentSetHistory.setComponentSetId(componentSetId);
        componentSetHistory.setTypeOperation(typeOperation);
        componentSetHistory.setComponentTypeHistory(componentTypeHistory);
        componentSetHistory.setValue(value);
        componentSetHistory.setIsActive(isActive);
        componentSetHistory.setUserHistory(userHistory);
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
