package com.rena.application.service.boiler.type;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.boiler.type.BoilerTypeHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoilerTypeHistoryService {
    private final BoilerTypeHistoryRepository boilerTypeHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addBoilerHistory(Long boilerId, Long componentNameSetId, String arcticle,
                                 Long boilerTypeAdditionalDataSetId,
                                 String oldTypeName, String model, String typeName, int typeOperation) {
        changeActiveStatusOld(boilerId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        BoilerTypeHistory boilerTypeHistory = new BoilerTypeHistory();
        boilerTypeHistory.setBoilerId(boilerId);
        boilerTypeHistory.setModel(model);
        boilerTypeHistory.setOdlTypeName(oldTypeName != null && typeOperation == 2 ? oldTypeName : null);
        boilerTypeHistory.setIsActive(true);
        boilerTypeHistory.setUserId(userHistory.getUserId());
        boilerTypeHistory.setModifiedDate(LocalDateTime.now());
        boilerTypeHistory.setComponentNameSetId(componentNameSetId);
        boilerTypeHistory.setTypeOperation(typeOperation);
        boilerTypeHistory.setTypeName(typeName);
        boilerTypeHistory.setArticle(arcticle);
        boilerTypeHistory.setBoilerTypeAdditionalDataSetId(boilerTypeAdditionalDataSetId);
        boilerTypeHistoryRepository.save(boilerTypeHistory);
    }

    private void changeActiveStatusOld(Long boilerId, int typeOperation)
    {
        if (typeOperation == 1 || boilerId == null) {
            return;
        }
        boilerTypeHistoryRepository.findByBoilerIdAndIsActive(boilerId, true).ifPresent((c) -> {
            c.setIsActive(false);
            boilerTypeHistoryRepository.save(c);
        });
    }
}
