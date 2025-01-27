package com.rena.application.service.component;

import com.rena.application.config.mapper.ComponentTypeMapper;
import com.rena.application.config.mapper.ComponentNameSetMapper;
import com.rena.application.config.mapper.ComponentSetMapper;
import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.dto.component.ComponentSetList;
import com.rena.application.entity.model.component.ComponentSet;
import com.rena.application.entity.model.component.ComponentType;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentNameSetRepository;
import com.rena.application.repository.component.ComponentTypeRepository;
import com.rena.application.repository.component.ComponentSetRepository;
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
public class ComponentSetService {
    private final ComponentSetRepository componentSetRepository;
    private final ComponentNameSetRepository componentNameSetRepository;
    private final ComponentTypeRepository componentTypeRepository;
    private final ComponentTypeMapper componentTypeMapper;
    private final ComponentSetMapper componentSetMapper;
    private final ComponentNameSetMapper componentNameSetMapper;
    private final ComponentSetHistoryService componentSetHistoryService;

    @Transactional
    public ComponentSetList getAllComponentsSet(Long componentNameSetId) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(componentNameSetId).orElseThrow(() ->
                new RecordNotFoundException("Не удалось найти набор компонентов"));
        List<ComponentSet> componentSet = componentSetRepository.findAllComponentsById(componentNameSetId);
        List<ComponentType> componentTypes = componentTypeRepository.findAll();
        List<ComponentSetDto> componentSetDtoList = componentSetMapper.toComponentSetDto(componentSet);
        return new ComponentSetList(
                componentNameSetMapper.toDto(componentNameSet),
                componentTypeMapper.toDto(componentTypes),
                componentSetDtoList);
    }

    @Transactional
    public void addComponentSet(Long ComponentNameSetId, ComponentSetDto componentSetDto) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(ComponentNameSetId).
                orElseThrow(() -> new RecordNotFoundException("Набор компонентов не найден"));
        ComponentType componentType = componentTypeRepository.findComponentTypeByName(componentSetDto.componentType().name()).
                orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
        ComponentSet componentSet = componentSetMapper.toEntity(componentSetDto);
        componentSet.setComponentType(componentType);
        componentSet.setComponentNameSet(componentNameSet);
        componentSetRepository.save(componentSet);
        componentSetHistoryService.addComponentHistory(componentSet.getId(), null, componentNameSet.getName(),
                componentType.getName(), componentSetDto.value(), true, 1);
    }

    @Transactional
    public void updateComponentSet(List<ComponentSetDto> componentsSetDto) {
        componentsSetDto.forEach(componentSetResponse -> {
            String componentTypeNameNew = componentSetResponse.componentType().name();
            ComponentSet componentSet = componentSetRepository.findByIdComponentSet(componentSetResponse.id()).
                    orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
            ComponentType componentType = componentTypeRepository.findComponentTypeByName(componentTypeNameNew).
                    orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
            String oldComponentTypeName = componentSet.getComponentType().getName();
            componentSetHistoryService.addComponentHistory(componentSet.getId(), oldComponentTypeName,
                    componentSet.getComponentNameSet().getName(),
                    componentTypeNameNew, componentSetResponse.value(), true, 2);
            componentSet.setComponentType(componentType);
            componentSet.setValue(componentSetResponse.value());
            componentSetRepository.save(componentSet);
        });
    }

    @Transactional
    public void deleteComponentSet(Long id) {
        ComponentSet componentSet = componentSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Компонент не найден"));
        componentSetHistoryService.addComponentHistory(componentSet.getId(), null,
                componentSet.getComponentNameSet().getName(),
                componentSet.getComponentType().getName(), componentSet.getValue(), true, 3);
        componentSetRepository.delete(componentSet);
    }

    @Transactional
    public void copyAllComponentsType(Long componentNameSetId) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(componentNameSetId).
                orElseThrow(() -> new RecordNotFoundException("Набор компонентов не найден"));
        var componentTypes = componentTypeRepository.findAll();
        componentTypes.forEach(componentType -> {
            var componentSet = new ComponentSet();
            componentSet.setComponentType(componentType);
            componentSet.setComponentNameSet(componentNameSet);
            componentSet.setValue("0");
            componentSetRepository.save(componentSet);
            componentSetHistoryService.addComponentHistory(componentSet.getId(), null, componentNameSet.getName(),
                    componentType.getName(), componentSet.getValue(), true, 1);
        });
    }
}
