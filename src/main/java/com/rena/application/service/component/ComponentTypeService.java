package com.rena.application.service.component;

import com.rena.application.config.mapper.ComponentTypeMapper;
import com.rena.application.entity.dto.component.ComponentTypeDto;
import com.rena.application.entity.model.component.ComponentType;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentTypeRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ComponentTypeService {
    private final ComponentTypeRepository componentTypeRepository;
    private final ComponentTypeMapper componentTypeMapper;

    public List<ComponentTypeDto> getAllComponents() {
        List<ComponentType> componentType = componentTypeRepository.findAll();
        return componentTypeMapper.toDto(componentType);
    }

    public ComponentTypeDto getComponent(Long id)
    {
        ComponentType componentType = componentTypeRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
        return componentTypeMapper.toComponentDto(componentType);
    }

    @Transactional
    public void addComponent(ComponentTypeDto componentTypeDto) {
        ComponentType componentType = componentTypeMapper.toEntity(componentTypeDto);
        componentTypeRepository.save(componentType);
    }

    @Transactional
    public void updateComponent(Long id, ComponentTypeDto componentTypeDto) {
        ComponentType componentType = componentTypeRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
        String oldComponentTypeName = componentType.getName();
        componentType.setName(componentTypeDto.name());
        componentTypeRepository.save(componentType);
    }

    @Transactional
    public void deleteComponent(Long id) {
        ComponentType componentType = componentTypeRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
        componentTypeRepository.delete(componentType);
    }
}
