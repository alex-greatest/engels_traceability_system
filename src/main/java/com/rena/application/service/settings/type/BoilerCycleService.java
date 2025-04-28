package com.rena.application.service.settings.type;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.type.BoilerTypeCycleRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoilerCycleService {
    private final BoilerTypeCycleRepository boilerTypeCycleRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addBoilerCycle(Long boilerId, String arcticle, String model, String typeName, int typeOperation,
                                 boolean isActive) {
        changeActiveStatusOld(boilerId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        BoilerTypeCycle boilerTypeCycle = new BoilerTypeCycle();
        boilerTypeCycle.setModel(model);
        boilerTypeCycle.setBoilerTypeId(boilerId);
        boilerTypeCycle.setUserHistory(userHistory);
        boilerTypeCycle.setModifiedDate(LocalDateTime.now());
        boilerTypeCycle.setTypeName(typeName);
        boilerTypeCycle.setArticle(arcticle);
        boilerTypeCycle.setIsActive(isActive);
        boilerTypeCycleRepository.save(boilerTypeCycle);
    }

    private void changeActiveStatusOld(Long boilerId, int typeOperation)
    {
        if (typeOperation == 1 || boilerId == null) {
            return;
        }
        boilerTypeCycleRepository.findByBoilerTypeIdAndIsActive(boilerId, true).ifPresent((c) -> {
            c.setIsActive(false);
            boilerTypeCycleRepository.save(c);
        });
    }
}
