package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalValueHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalValueHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoilerTypeAdditionalValueHistoryService {
    private final BoilerTypeAdditionalValueHistoryRepository boilerTypeAdditionalValueHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addBoilerTypeAdditionDataValueHistory(Long boilerTypeAdditionDataSetId,
                                                      Long boilerTypeAdditionalDataId,
                                                      Long boilerAdditionalValueId, String oldValue, String value,
                                                      String unit, boolean isActive, int typeOperation) {
        changeActiveStatusOld(boilerAdditionalValueId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        var boilerTypeAdditionalValueHistory = new BoilerTypeAdditionalValueHistory();
        boilerTypeAdditionalValueHistory.setBoilerTypeAdditionalDataId(boilerTypeAdditionalDataId);
        boilerTypeAdditionalValueHistory.setBoilerTypeAdditionalDataSetId(boilerTypeAdditionDataSetId);
        boilerTypeAdditionalValueHistory.setValue(value);
        boilerTypeAdditionalValueHistory.setOldValue(oldValue != null && typeOperation == 2 ? oldValue : null);
        boilerTypeAdditionalValueHistory.setUnit(unit);
        boilerTypeAdditionalValueHistory.setIsActive(isActive);
        boilerTypeAdditionalValueHistory.setUserHistoryId(userHistory.getUserId());
        boilerTypeAdditionalValueHistory.setModifiedDate(LocalDateTime.now());
        boilerTypeAdditionalValueHistory.setTypeOperation(typeOperation);
        boilerTypeAdditionalValueHistory.setBoilerTypeAdditionalValueId(boilerAdditionalValueId);
        boilerTypeAdditionalValueHistoryRepository.save(boilerTypeAdditionalValueHistory);
    }

    private void changeActiveStatusOld(Long boilerTypeAdditionalDataId, int typeOperation)
    {
        if (typeOperation == 1 || boilerTypeAdditionalDataId == null) {
            return;
        }
        boilerTypeAdditionalValueHistoryRepository.findByBoilerTypeAdditionalValueIdAndIsActive(boilerTypeAdditionalDataId,
                        true).
                ifPresent((c) -> {
                    c.setIsActive(false);
                    boilerTypeAdditionalValueHistoryRepository.save(c);
                });
    }
}
