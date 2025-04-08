package com.rena.application.service.user;

import com.rena.application.config.mapper.component.common.UserMapper;
import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.dto.user.*;
import com.rena.application.entity.dto.user.station.OperatorRequestAuthorization;
import com.rena.application.entity.dto.user.station.UserRequestAuthorization;
import com.rena.application.entity.model.user.Role;
import com.rena.application.entity.model.user.User;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.common.StationRepository;
import com.rena.application.repository.user.RoleRepository;
import com.rena.application.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserHistoryService userHistoryService;
    private final UserInfoService userInfoService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StationRepository stationRepository;
    private final UserLoginLogService userLoginLogService;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        UserInfo userInfo = userInfoService.getUserInfo();
        String role = userInfo.authorities().getFirst();
        List<User> users = switch (role) {
            case "ROLE_Администратор" -> {
                if (userInfo.name().equals("admin")) {
                    yield userRepository.findAllActiveUsers();
                }
                yield userRepository.findByIs_deletedFalseAndUsernameNotAndRole_NameIn(RoleListHelper.getRolesAdmin());
            }
            case "ROLE_Бригадир", "ROLE_Мастер/Технолог" -> userRepository.findByIs_deletedFalseAndUsernameNotAndRole_NameIn(RoleListHelper.getRolesEngineer());
            default -> throw new DbException("Не удалось получить список пользователей");
        };
        return users.stream().map((u) -> {
            String newRoleName = u.getRole().getName().replace("ROLE_", "");
            u.getRole().setName(newRoleName);
            return userMapper.toDto(u);
        }).toList();
    }

    public UserResponse getUser(Integer code)
    {
        User user = userRepository.findByCode(code).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }

    @Transactional
    public void addUser(UserRequest userRequest) {
        String roleName = String.format("ROLE_%s", userRequest.role().name());
        Role role = roleRepository.findByName(roleName);
        var user = userMapper.toEntity(userRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        userHistoryService.addUserHistory(user.getId(), null, null, user.getCode(),
                user, role, 1, true);
    }

    @Transactional
    public void updateUser(Integer code, UserRequestUpdate userRequest) {
        String roleName = String.format("ROLE_%s", userRequest.role().name());
        Role role = roleRepository.findByName(roleName);
        User user = userRepository.findByCode(code).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String oldUsername = user.getUsername();
        Integer oldCode = user.getCode();
        user.setCode(userRequest.code());
        user.setUsername(userRequest.username());
        user.setRole(role);
        userHistoryService.addUserHistory(user.getId(),
                oldCode, oldUsername, code, user, role, 2, true);
        userRepository.save(user);
    }

    @Transactional
    public void updatePasswordUser(Integer code, UserRequestPassword userRequest) {
        User user = userRepository.findByCode(code).orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.password()));
        userHistoryService.addUserHistory(user.getId(),
                code, null, code, user, user.getRole(), 4, true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer code) {
        User user = userRepository.findByCode(code).orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userHistoryService.addUserHistory(user.getId(), code, null, code,
                user, user.getRole(), 3, false);
        userRepository.delete(user);
    }

    /**
     * Метод для авторизации оператора. Пока такой
     *
     * @param userRequestAuthorization - запрос авторизации
     * @return - ответ с данными пользователя
     */
    @Transactional
    public UserResponse getOperatorAuthorization(@Valid OperatorRequestAuthorization userRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(userRequestAuthorization.login()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        if (!bCryptPasswordEncoder.matches(userRequestAuthorization.password(), user.getPassword())) {
            throw new RecordNotFoundException("Неверный пароль");
        }
        userLoginLogService.saveUserLoginLog(user.getId(), userRequestAuthorization.station(), true);
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }

    /**
     * Метод для выхода оператора. Пока такой
     *
     * @param operatorRequestAuthorization - запрос авторизации
     */
    @Transactional
    public void stationLogout(@Valid OperatorRequestAuthorization operatorRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(operatorRequestAuthorization.login()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userLoginLogService.saveUserLoginLog(user.getId(), operatorRequestAuthorization.station(), false);
    }

    @Transactional
    public UserResponse getUserAuthorization(@Valid UserRequestAuthorization userRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(userRequestAuthorization.login()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        String newRoleName = user.getRole().getName().replace("ROLE_", "");
        if (!bCryptPasswordEncoder.matches(userRequestAuthorization.password(), user.getPassword())) {
            throw new RecordNotFoundException("Неверный пароль");
        }
        if (newRoleName.equals("Сборщик")) {
            throw new RecordNotFoundException("Доступ запрещён");
        }
        userLoginLogService.saveUserLoginLog(user.getId(), userRequestAuthorization.station(), true);
        return userMapper.toDtoWithCustomRoleName(user, newRoleName);
    }


    @Transactional
    public void stationLogout(@Valid UserRequestAuthorization userRequestAuthorization) {
        User user = userRepository.findByUsernameAuthorization(userRequestAuthorization.login()).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userLoginLogService.saveUserLoginLog(user.getId(), userRequestAuthorization.station(), false);
    }
}
