package com.rena.application.service.traceability.common.user;

import com.rena.application.config.mapper.component.common.UserMapper;
import com.rena.application.entity.dto.settings.user.UserResponse;
import com.rena.application.entity.dto.settings.user.station.OperatorRequestAuthorization;
import com.rena.application.entity.model.settings.user.User;
import com.rena.application.entity.model.traceability.common.log.UserLoginLog;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.user.UserRepository;
import com.rena.application.service.settings.user.UserLoginLogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserTraceabilityService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserLoginLogService userLoginLogService;

    /**
     * Метод для авторизации оператора. Пока такой
     *
     * @param userRequestAuthorization - запрос авторизации
     * @return - ответ с данными пользователя
     */
    @Transactional
    public UserResponse getOperatorAuthorization(@Valid OperatorRequestAuthorization userRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(userRequestAuthorization.getLogin()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        if (!bCryptPasswordEncoder.matches(userRequestAuthorization.getPassword(), user.getPassword())) {
            throw new RecordNotFoundException("Неверный пароль");
        }
        userLoginLogService.saveOperatorLoginLog(user, userRequestAuthorization.getStation(), true);
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }

    /**
     * Метод для выхода оператора. Пока такой
     *
     */
    @Transactional
    public void stationOperatorLogout(@NotBlank String login) {
        User user = userRepository.findByUsernameAuthorization(login).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userLoginLogService.operatorLogout(user.getId());
    }

    @Transactional
    public UserResponse getAdminAuthorization(@Valid OperatorRequestAuthorization operatorRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(operatorRequestAuthorization.getLogin()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        if (!bCryptPasswordEncoder.matches(operatorRequestAuthorization.getPassword(), user.getPassword())) {
            throw new RecordNotFoundException("Неверный пароль");
        }
        if (newRoleName.equals("Оператор")) {
            throw new RecordNotFoundException("Доступ запрещён");
        }
        userLoginLogService.saveAdminLoginLog(user, operatorRequestAuthorization.getStation(), true);
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }

    @Transactional
    public void stationAdminLogout(@NotBlank String login) {
        User user = userRepository.findByUsernameAuthorization(login).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userLoginLogService.adminLogout(user.getId());
    }

    @Transactional
    public UserResponse getLastOperatorLogin(String stationName) {
        var operatorLoginLogOptional = userLoginLogService.getLastOperatorLogin(stationName);
        if (operatorLoginLogOptional.isPresent()) {
            var username = operatorLoginLogOptional.get().getUser().getUsername();
            var userOptional = userRepository.findByUsernameAuthorization(username);
            return getUserResponseLast(username, userOptional, operatorLoginLogOptional.get());
        }
        return null;
    }

    @Transactional
    public UserResponse getLastAdminLogin(String stationName) {
        var adminLoginLogOptional = userLoginLogService.getLastAdminLogin(stationName);
        if (adminLoginLogOptional.isPresent()) {
            var username = adminLoginLogOptional.get().getUser().getUsername();
            var userOptional = userRepository.findByUsernameAuthorization(username);
            return getUserResponseLast(username, userOptional, adminLoginLogOptional.get());
        }
        return null;
    }

    @Transactional
    public UserResponse getUserResponseLast(String login, Optional<User> userOptional, UserLoginLog userLoginLog) {
        if (userOptional.isEmpty()) {
            userLoginLog.setIsLogin(false);
            userLoginLog.setDateLogout(userLoginLog.getDateLogin());
            return null;
        }
        var user = userOptional.get();
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }
}
