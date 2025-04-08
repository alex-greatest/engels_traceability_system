package com.rena.application.config.mapper.component.common;

import com.rena.application.entity.dto.user.RoleDTO;
import com.rena.application.entity.dto.user.UserRequest;
import com.rena.application.entity.dto.user.UserRequestUpdate;
import com.rena.application.entity.dto.user.UserResponse;
import com.rena.application.entity.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    List<UserResponse> toDto(List<User> user);

    UserResponse toDto(User user);

    User toEntity(UserRequest userRequest);

    UserRequest toUserRequest(User user);

    User toEntity(UserRequestUpdate userRequestUpdate);

    UserRequestUpdate toDtoUserUpdate(User user);

    // Метод для передачи кастомного имени роли
    default UserResponse toDtoWithCustomRoleName(User user, String customRoleName) {
        if (user == null) {
            return null;
        }

        String username = user.getUsername();
        Integer code = user.getCode();
        RoleDTO role = new RoleDTO(customRoleName);

        return new UserResponse(username, code, role);
    }
}