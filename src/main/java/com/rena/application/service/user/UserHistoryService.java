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

    public void addUserHistory(Long userId, Integer oldCode, String oldUsername,
                               Integer code, User user, Role role, int typeOperation) {
        changeActiveStatusOld(userId, typeOperation);
        UserInfo userInfo = userInfoService.getUserInfo();
        UserHistory userHistory = new UserHistory();
        userHistory.setCode(user.getCode());
        userHistory.setUsername(user.getUsername());
        userHistory.setPassword(user.getPassword());
        userHistory.setRole(role);
        userHistory.setModifiedDate(LocalDateTime.now());
        userHistory.setCodeChanged(userInfo.code());
        userHistory.setUsernameChanged(userInfo.name());
        userHistory.setIsActive(true);
        userHistory.setTypeOperation(typeOperation);
        userHistory.setOldCode(oldCode != null && typeOperation == 2 ? oldCode : null);
        userHistory.setOldUsername(oldUsername != null && typeOperation == 2 ? oldUsername : null);
        userHistory.setUserId(userId);
        userHistoryRepository.save(userHistory);
    }

    private void changeActiveStatusOld(Long userId, int typeOperation)
    {
        if (typeOperation == 1 || userId == null) {
            return;
        }
        userHistoryRepository.findByUserIdAndIsActive(userId, true).ifPresent((u) -> {
            u.setIsActive(false);
            userHistoryRepository.save(u);
        });
    }
}
