package com.rena.application.service.component;

import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.entity.model.component.ComponentNameSetHistory;
import com.rena.application.exceptions.DbException;
import com.rena.application.repository.component.ComponentNameSetHistoryRepository;
import com.rena.application.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ComponentNameSetHistoryService {
    private final ComponentNameSetHistoryRepository componentNameSetHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;

    public void addComponentHistory(String name, ComponentNameSet componentNameSet, boolean isActive, int typeOperation) {
        changeActiveStatusOld(name, typeOperation);
        var userInfo = userInfoService.getUserInfo();
        var user = userRepository.findByCode(userInfo.code()).
                orElseThrow(() -> new DbException("Не удалось сохранить набор компонентов. Не найден пользователь"));
        var componentHistory = new ComponentNameSetHistory();
        componentHistory.setName(componentNameSet.getName());
        componentHistory.setModifiedDate(LocalDateTime.now());
        componentHistory.setIsActive(isActive);
        componentHistory.setTypeOperation(typeOperation);
        componentHistory.setUser(user);
        componentNameSetHistoryRepository.save(componentHistory);
    }

    private void changeActiveStatusOld(String name, int typeOperation)
    {
        if (typeOperation == 1) {
            return;
        }
        componentNameSetHistoryRepository.findByNameAndIsActive(name, true).ifPresent((c) -> {
            c.setIsActive(false);
            componentNameSetHistoryRepository.save(c);
        });
    }
}
