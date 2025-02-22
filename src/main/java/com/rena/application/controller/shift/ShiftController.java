package com.rena.application.controller.shift;

import com.rena.application.entity.dto.shift.ShiftDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.shift.ShiftService;
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
public class ShiftController {
    private final ShiftService shiftService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ShiftDto> getAllShifts() {
        return shiftService.getAllShifts();
    }

    public void addShift(@Nonnull @Valid ShiftDto shiftDto) {
        try {
            shiftService.addShift(shiftDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении смены", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateShift(@Nonnull @NotNull Integer oldShiftNumber,
                            @Nonnull @Valid ShiftDto shiftDto) {
        try {
            shiftService.updateShift(oldShiftNumber, shiftDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении смены", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteShift(@Nonnull @NotNull Integer shiftNumber) {
        try {
            shiftService.deleteShift(shiftNumber);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении имени набора компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
