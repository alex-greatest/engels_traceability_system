package com.rena.application.service.component;

import com.rena.application.entity.dto.user.*;
import com.rena.application.entity.model.component.Component;
import com.rena.application.exceptions.DbException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.ComponentRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentHistoryService componentHistoryService;
    private final ComponentMapper componentMapper;

    public List<ComponentDto> getAllComponents() {
        List<Component> component = componentRepository.findAll();
        return componentMapper.toDto(component);
    }

    public ComponentDto getComponent(Long id)
    {
        Component component = componentRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
        return componentMapper.toComponentDto(component);
    }

    @Transactional
    public void addComponent(ComponentDto componentDto) {
        Component component = componentMapper.toEntity(componentDto);
        componentHistoryService.addComponentHistory(component.getName(), component, true, 1);
        componentRepository.save(component);
    }

    @Transactional
    public void updateComponent(Long id, String oldNameComponent, ComponentDto componentDto) {
        Component component = componentRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
        component.setName(componentDto.name());
        componentHistoryService.addComponentHistory(oldNameComponent, component, true, 2);
        componentRepository.save(component);
    }

    @Transactional
    public void deleteComponent(Long id) {
        Component component = componentRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
        componentHistoryService.addComponentHistory(component.getName(), component, false, 3);
        componentRepository.delete(component);
    }
}
