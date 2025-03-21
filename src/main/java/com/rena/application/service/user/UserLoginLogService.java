package com.rena.application.service.user;

import com.rena.application.entity.model.result.station.UserLoginLog;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.common.StationRepository;
import com.rena.application.repository.result.common.UserLoginLogRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserLoginLogService {
    private final UserLoginLogRepository userLoginLogRepository;
    private final StationRepository stationRepository;
    private final UserHistoryRepository userHistoryRepository;

    public void saveUserLoginLog(Long idUser, String stationName, boolean isLogin) {
        var userHistory = userHistoryRepository.findByUserIdAndIsActive(idUser, true).
                orElseThrow(() -> new RecordNotFoundException("История пользователя не найдена"));
        var station = stationRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var userLoginLog = new UserLoginLog();
        userLoginLog.setUserHistory(userHistory);
        userLoginLog.setStation(station);
        userLoginLog.setDate(LocalDateTime.now());
        userLoginLog.setIsLogin(isLogin);
        userLoginLogRepository.save(userLoginLog);
    }
}
