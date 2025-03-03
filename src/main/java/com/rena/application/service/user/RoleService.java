package com.rena.application.service.user;

import com.rena.application.config.mapper.component.common.RoleMapper;
import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.rena.application.entity.dto.user.RoleDTO;
import com.rena.application.entity.model.user.Role;
import com.rena.application.exceptions.DbException;
import com.rena.application.repository.user.RoleRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserInfoService userInfoService;

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        UserInfo userInfo = userInfoService.getUserInfo();
        String role = userInfo.authorities().getFirst();
        List<Role> roles = switch (role) {
            case "ROLE_Администратор" -> {
                if (userInfo.name().equals("admin")) {
                    yield roleRepository.findAll();
                }
                yield roleRepository.findByNameIn(RoleListHelper.getRolesAdmin());
            }
            case "ROLE_Инженер МОЕ", "ROLE_Инженер TEF" -> roleRepository.findByNameIn(RoleListHelper.getRolesEngineer());
            default -> throw new DbException("Не удалось получить список пользователей");
        };
        return roles.stream().map((r) -> {
            String newRoleName = r.getName().replace("ROLE_", "");
            r.setName(newRoleName);
            return roleMapper.toDto(r);
        }).toList();
    }
}
