package com.rena.application.service.user;

import com.rena.application.config.mapper.UserMapper;
import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.dto.user.UserRequest;
import com.rena.application.entity.dto.user.UserRequestPassword;
import com.rena.application.entity.dto.user.UserRequestUpdate;
import com.rena.application.entity.dto.user.UserResponse;
import com.rena.application.entity.model.user.Role;
import com.rena.application.entity.model.user.User;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.user.RoleRepository;
import com.rena.application.repository.user.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserHistoryService userHistoryService;
    private final UserInfoService userInfoService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
            case "ROLE_Инженер МОЕ", "ROLE_Инженер TEF" -> userRepository.findByIs_deletedFalseAndUsernameNotAndRole_NameIn(RoleListHelper.getRolesEngineer());
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
        user.getRole().setName(newRoleName);
        return userMapper.toDto(user);
    }

    @Transactional
    public void addUser(UserRequest userRequest) {
        String roleName = String.format("ROLE_%s", userRequest.role().name());
        Role role = roleRepository.findByName(roleName);
        var user = userMapper.toEntity(userRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userHistoryService.addUserHistory(user.getCode(), user, role, true, 1);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Integer code, UserRequestUpdate userRequest) {
        String roleName = String.format("ROLE_%s", userRequest.role().name());
        Role role = roleRepository.findByName(roleName);
        User user = userRepository.findByCode(code).
                orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        user.setCode(userRequest.code());
        user.setUsername(userRequest.username());
        user.setRole(role);
        userHistoryService.addUserHistory(code, user, role, true, 2);
        userRepository.save(user);
    }

    @Transactional
    public void updatePasswordUser(Integer code, UserRequestPassword userRequest) {
        User user = userRepository.findByCode(code).orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.password()));
        userHistoryService.addUserHistory(code, user, user.getRole(), true, 2);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer code) {
        User user = userRepository.findByCode(code).orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        userHistoryService.addUserHistory(code, user, user.getRole(), false, 3);
        userRepository.delete(user);
    }
}
