package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSetHistory;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetHistoryRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoilerTypeAdditionalDataSetHistoryService {
    private final BoilerTypeAdditionalDataSetHistoryRepository boilerTypeAdditionalDataSetHistoryRepository;
    private final UserInfoService userInfoService;
    private final UserHistoryRepository userHistoryRepository;

    public BoilerTypeAdditionalDataSetHistory addBoilerTypeAdditionDataNameSetHistory(Long boilerTypeAdditionDataSetId,
                                                                                      String oldNameNameSet, String nameNameSet,
                                                                                      boolean isActive, int typeOperation) {
        changeActiveStatusOld(boilerTypeAdditionDataSetId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = userHistoryRepository.findByCodeAndIsActive(userInfo.code(), true).
                orElseThrow(() -> new RecordNotFoundException("Не удалось сохранить историю. Пользователь не найден"));
        var boilerTypeAdditionalSetHistory = new BoilerTypeAdditionalDataSetHistory();
        boilerTypeAdditionalSetHistory.setBoilerTypeAdditionDataSetId(boilerTypeAdditionDataSetId);
        boilerTypeAdditionalSetHistory.setName(nameNameSet);
        boilerTypeAdditionalSetHistory.setOldName(oldNameNameSet != null && typeOperation == 2 ? oldNameNameSet : null);;
        boilerTypeAdditionalSetHistory.setIsActive(isActive);
        boilerTypeAdditionalSetHistory.setUserId(userHistory.getUserId());
        boilerTypeAdditionalSetHistory.setModifiedDate(LocalDateTime.now());
        boilerTypeAdditionalSetHistory.setTypeOperation(typeOperation);
        return boilerTypeAdditionalDataSetHistoryRepository.save(boilerTypeAdditionalSetHistory);
    }

    private void changeActiveStatusOld(Long boilerTypeAdditionDataSetId, int typeOperation)
    {
        if (typeOperation == 1 || boilerTypeAdditionDataSetId == null) {
            return;
        }
        boilerTypeAdditionalDataSetHistoryRepository.findByBoilerTypeAdditionDataSetIdAndIsActive(boilerTypeAdditionDataSetId,
                true).ifPresent((c) -> {
            c.setIsActive(false);
            boilerTypeAdditionalDataSetHistoryRepository.save(c);
        });
    }
}
