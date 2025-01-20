package com.rena.application.service.user;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.model.user.Role;
import com.rena.application.entity.model.user.User;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;
    private final UserInfoService userInfoService;

    public void addUserHistory(Integer code, User user, Role role, boolean isActive, int typeOperation) {
        changeActiveStatusOld(code, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = new UserHistory();
        userHistory.setCode(user.getCode());
        userHistory.setUsername(user.getUsername());
        userHistory.setPassword(user.getPassword());
        userHistory.setRole(role);
        userHistory.setModifiedDate(LocalDateTime.now());
        userHistory.setCodeChanged(userInfo.code());
        userHistory.setUsernameChanged(userInfo.name());
        userHistory.setIsActive(isActive);
        userHistory.setTypeOperation(typeOperation);
        userHistoryRepository.save(userHistory);
    }

    private void changeActiveStatusOld(Integer code, int typeOperation)
    {
        if (typeOperation == 1) {
            return;
        }
        userHistoryRepository.findByCodeAndIsActive(code, true).ifPresent((u) -> {
            u.setIsActive(false);
            userHistoryRepository.save(u);
        });
    }
}
