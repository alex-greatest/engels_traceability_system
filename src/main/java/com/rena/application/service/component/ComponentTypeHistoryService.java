package com.rena.application.service.component;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.ComponentTypeHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentTypeHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ComponentTypeHistoryService {
    private final ComponentTypeHistoryRepository componentTypeHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addComponentTypeHistory(String oldNameComponentType, String nameComponentType, boolean isActive, int typeOperation) {
        changeActiveStatusOld(oldNameComponentType, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        ComponentTypeHistory componentTypeHistory = new ComponentTypeHistory();
        componentTypeHistory.setTypeOperation(typeOperation);
        componentTypeHistory.setName(nameComponentType);
        componentTypeHistory.setOldName(oldNameComponentType != null && typeOperation == 2 ? oldNameComponentType : null);
        componentTypeHistory.setIsActive(isActive);
        componentTypeHistory.setUserHistory(userHistory);
        componentTypeHistory.setModifiedDate(LocalDateTime.now());
        componentTypeHistoryRepository.save(componentTypeHistory);
    }

    private void changeActiveStatusOld(String oldNameComponentType, int typeOperation)
    {
        if (typeOperation == 1 || oldNameComponentType == null) {
            return;
        }
        componentTypeHistoryRepository.findByNameAndIsActive(oldNameComponentType, true).ifPresent((c) -> {
            c.setIsActive(false);
            componentTypeHistoryRepository.save(c);
        });
    }
}
