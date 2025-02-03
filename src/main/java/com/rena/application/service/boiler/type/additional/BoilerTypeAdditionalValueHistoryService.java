package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.boiler.BoilerTypeAdditionalValueHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetHistoryRepository;
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
    private final BoilerTypeAdditionalDataRepository boilerTypeAdditionalDataRepository;
    private final BoilerTypeAdditionalDataSetHistoryRepository boilerTypeAdditionalDataSetHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public void addBoilerTypeAdditionDataValueHistory(Long boilerTypeAdditionDataSetId, Long boilerTypeAdditionalDataId,
                                                      String oldValue, String value,
                                                      String unit, boolean isActive,
                                                      int typeOperation) {
        changeActiveStatusOld(boilerTypeAdditionDataSetId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        var boilerTypeAdditionalData = boilerTypeAdditionalDataRepository.findById(boilerTypeAdditionalDataId).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Значение не найдено."));
        var boilerTypeAdditionDataSet = boilerTypeAdditionalDataSetHistoryRepository.
                findByBoilerTypeAdditionDataSetIdAndIsActive(boilerTypeAdditionDataSetId, true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Набор данных не найден."));
        var boilerTypeAdditionalValueHistory = new BoilerTypeAdditionalValueHistory();
        boilerTypeAdditionalValueHistory.setBoilerTypeAdditionalData(boilerTypeAdditionalData);
        boilerTypeAdditionalValueHistory.setBoilerTypeAdditionalDataSet(boilerTypeAdditionDataSet);
        boilerTypeAdditionalValueHistory.setValue(value);
        boilerTypeAdditionalValueHistory.setOldValue(oldValue != null && typeOperation == 2 ? oldValue : null);
        boilerTypeAdditionalValueHistory.setUnit(unit);
        boilerTypeAdditionalValueHistory.setIsActive(isActive);
        boilerTypeAdditionalValueHistory.setUserHistory(userHistory);
        boilerTypeAdditionalValueHistory.setModifiedDate(LocalDateTime.now());
        boilerTypeAdditionalValueHistory.setTypeOperation(typeOperation);
        boilerTypeAdditionalValueHistoryRepository.save(boilerTypeAdditionalValueHistory);
    }

    private void changeActiveStatusOld(Long boilerTypeAdditionDataSetId, int typeOperation)
    {
        if (typeOperation == 1 || boilerTypeAdditionDataSetId == null) {
            return;
        }
        boilerTypeAdditionalValueHistoryRepository.findByBoilerTypeAdditionalValueAndIsActive(boilerTypeAdditionDataSetId, true).
                ifPresent((c) -> {
                    c.setIsActive(false);
                    boilerTypeAdditionalValueHistoryRepository.save(c);
                });
    }
}
