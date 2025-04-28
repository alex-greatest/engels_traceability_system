package com.rena.application.service.settings.user;

import com.rena.application.entity.model.traceability.common.log.AdminLoginLog;
import com.rena.application.entity.model.traceability.common.log.OperatorLoginLog;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.log.AdminLoginLogRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.repository.traceability.common.log.OperatorLoginLogRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserLoginLogService {
    private final OperatorLoginLogRepository operatorLoginLogRepository;
    private final AdminLoginLogRepository adminLoginLogRepository;
    private final StationRepository stationRepository;
    private final UserHistoryRepository userHistoryRepository;

    public void saveOperatorLoginLog(Long idUser, String stationName, boolean isLogin) {
        var userHistory = userHistoryRepository.findByUserIdAndIsActive(idUser, true).
                orElseThrow(() -> new RecordNotFoundException("История пользователя не найдена"));
        var station = stationRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var userLoginLog = new OperatorLoginLog();
        userLoginLog.setUserHistory(userHistory);
        userLoginLog.setStation(station);
        userLoginLog.setDateLogin(LocalDateTime.now());
        userLoginLog.setIsLogin(isLogin);
        operatorLoginLogRepository.save(userLoginLog);
    }

    public void operatorLogout(Long idUser) {
        operatorLoginLogRepository.findByUserHistory_Username(idUser).ifPresent(operatorLoginLog -> {
            operatorLoginLog.setDateLogout(LocalDateTime.now());
            operatorLoginLog.setIsLogin(false);
        });
    }

    public void saveAdminLoginLog(Long idUser, String stationName, boolean isLogin) {
        var userHistory = userHistoryRepository.findByUserIdAndIsActive(idUser, true).
                orElseThrow(() -> new RecordNotFoundException("История пользователя не найдена"));
        var station = stationRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var adminLoginLog = new AdminLoginLog();
        adminLoginLog.setUserHistory(userHistory);
        adminLoginLog.setStation(station);
        adminLoginLog.setDateLogin(LocalDateTime.now());
        adminLoginLog.setIsLogin(isLogin);
        adminLoginLogRepository.save(adminLoginLog);
    }

    public void adminLogout(Long idUser) {
        adminLoginLogRepository.findByUserHistory_UserId(idUser).ifPresent(operatorLoginLog -> {
            operatorLoginLog.setDateLogout(LocalDateTime.now());
            operatorLoginLog.setIsLogin(false);
        });
    }

    public Optional<OperatorLoginLog> getLastOperatorLogin(String stationName) {
        return operatorLoginLogRepository.findByStation_Name(stationName);
    }

    public Optional<AdminLoginLog> getLastAdminLogin(String stationName) {
        return adminLoginLogRepository.findByStation_Name(stationName);
    }
}
