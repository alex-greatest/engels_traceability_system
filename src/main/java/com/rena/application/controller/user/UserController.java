package com.rena.application.controller.user;

import com.rena.application.entity.dto.user.UserRequest;
import com.rena.application.entity.dto.user.UserRequestPassword;
import com.rena.application.entity.dto.user.UserRequestUpdate;
import com.rena.application.entity.dto.user.UserResponse;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.user.UserService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class UserController {
    private final UserService userService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Nonnull
    public UserResponse getUser(@Nonnull @NotNull Integer code)
    {
        return userService.getUser(code);
    }

    public void addUser(@Valid UserRequest userRequest) {
        try {
            userService.addUser(userRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении пользователя", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateUser(@Nonnull @NotNull Integer code, @Valid UserRequestUpdate userRequest) {
        try {
            userService.updateUser(code, userRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при обновлении пользователя", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updatePasswordUser(@Nonnull @NotNull Integer code, @Valid UserRequestPassword userRequest) {
        try {
            userService.updatePasswordUser(code, userRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при обновлении пароля пользователя", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteUser(@Nonnull @NotNull Integer code) {
        try {
            userService.deleteUser(code);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении пользователя", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
