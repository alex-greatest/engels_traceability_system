package com.rena.application.service.boiler;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.boiler.BoilerHistory;
import com.rena.application.entity.model.component.ComponentNameSetHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.BoilerHistoryRepository;
import com.rena.application.repository.component.ComponentNameSetHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoilerHistoryService {
    private final ComponentNameSetHistoryRepository componentNameSetHistoryRepository;
    private final BoilerHistoryRepository boilerHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addBoilerHistory(Long boilerId, String oldArticle,
                                    String componentNameSet, String article, String name,
                                    boolean isActive, int typeOperation) {
        changeActiveStatusOld(boilerId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        ComponentNameSetHistory componentNameSetHistory =
                componentNameSetHistoryRepository.findByNameAndIsActive(componentNameSet, true).
                        orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Набор компонентов не найден"));
        BoilerHistory boilerHistory = new BoilerHistory();
        boilerHistory.setBoilerId(boilerId);
        boilerHistory.setArticle(article);
        boilerHistory.setOldArticle(oldArticle != null && typeOperation == 2 ? oldArticle : null);
        boilerHistory.setIsActive(isActive);
        boilerHistory.setUserHistory(userHistory);
        boilerHistory.setModifiedDate(LocalDateTime.now());
        boilerHistoryRepository.save(boilerHistory);
    }

    private void changeActiveStatusOld(Long boilerId, int typeOperation)
    {
        if (typeOperation == 1 || boilerId == null) {
            return;
        }
        boilerHistoryRepository.findByBoilerIdAndIsActive(boilerId, true).ifPresent((c) -> {
            c.setIsActive(false);
            boilerHistoryRepository.save(c);
        });
    }
}
