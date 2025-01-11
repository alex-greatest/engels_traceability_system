package com.rena.application.service.component;

import com.rena.application.entity.dto.user.*;
import com.rena.application.entity.model.component.Component;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.ComponentRepository;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentHistoryService componentHistoryService;
    private final ComponentMapper componentMapper;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentDto> getAllComponents() {
        List<Component> component = componentRepository.findAll();
        return componentMapper.toDto(component);
    }

    @Nonnull
    public ComponentDto getComponent(@Nonnull Long id)
    {
        Component component = componentRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
        return componentMapper.toComponentDto(component);
    }

    @Transactional
    public void addComponent(@Valid ComponentDto componentDto) {
        try {
            Component component = componentMapper.toEntity(componentDto);
            componentHistoryService.addComponentHistory(component.getName(), component, true, 1);
            componentRepository.save(component);
        } catch (DataAccessException e) {
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    @Transactional
    public void updateComponent(@Nonnull Long id, @Valid ComponentDto componentDto) {
        try {
            Component component = componentRepository.findById(id).
                    orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
            componentHistoryService.addComponentHistory(component.getName(), component, true, 2);
            componentRepository.save(component);
        } catch (DataAccessException e) {
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    @Transactional
    public void deleteComponent(@Nonnull Long id) {
        try {
            Component component = componentRepository.findById(id).
                    orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
            componentHistoryService.addComponentHistory(component.getName(), component, false, 3);
            componentRepository.save(component);
        } catch (DataAccessException e) {
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
