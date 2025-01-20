package com.rena.application.service.component;

import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.Component;
import com.rena.application.entity.model.component.ComponentHistory;
import com.rena.application.exceptions.DbException;
import com.rena.application.repository.component.ComponentHistoryRepository;
import com.rena.application.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ComponentHistoryService {
    private final ComponentHistoryRepository componentHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;

    public void addComponentHistory(String name, Component component, boolean isActive, int typeOperation) {
        changeActiveStatusOld(name, typeOperation);
        var userInfo = userInfoService.getUserInfo();
        var user = userRepository.findByCode(userInfo.code()).
                orElseThrow(() -> new DbException("Не удалось сохранить компонент. Не найден пользователь"));
        var componentHistory = new ComponentHistory();
        componentHistory.setName(component.getName());
        componentHistory.setModifiedDate(LocalDateTime.now());
        componentHistory.setIsActive(isActive);
        componentHistory.setTypeOperation(typeOperation);
        componentHistory.setUser(user);
        componentHistoryRepository.save(componentHistory);
    }

    private void changeActiveStatusOld(String name, int typeOperation)
    {
        if (typeOperation == 1) {
            return;
        }
        componentHistoryRepository.findByNameAndIsActive(name, true).ifPresent((c) -> {
            c.setIsActive(false);
            componentHistoryRepository.save(c);
        });
    }
}
