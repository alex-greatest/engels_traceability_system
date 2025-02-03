package com.rena.application.service.component;

import com.rena.application.config.mapper.ComponentNameSetMapper;
import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentNameSetRepository;
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
public class ComponentNameSetService {
    private final ComponentNameSetRepository componentNameSetRepository;
    private final ComponentNameSetMapper componentNameSetMapper;
    private final ComponentNameSetHistoryService componentNameSetHistoryService;

    public List<ComponentNameSetDto> getAllComponents() {
        List<ComponentNameSet> componentNameSets = componentNameSetRepository.findAll();
        return componentNameSetMapper.toDto(componentNameSets);
    }

    public ComponentNameSetDto getComponent(Long id)
    {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Название набора компонентов не найдено"));
        return componentNameSetMapper.toDto(componentNameSet);
    }

    @Transactional
    public void addComponent(ComponentNameSetDto componentNameSetDto) {
        ComponentNameSet componentNameSet = componentNameSetMapper.toEntity(componentNameSetDto);
        componentNameSetHistoryService.addComponentNameSetHistory(null, componentNameSetDto.name(), true, 1);
        componentNameSetRepository.save(componentNameSet);
    }

    @Transactional
    public void updateComponent(Long id, ComponentNameSetDto componentNameSetDto) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        String oldNameNameSet = componentNameSet.getName();
        componentNameSetHistoryService.addComponentNameSetHistory(oldNameNameSet, componentNameSetDto.name(), true, 2);
        componentNameSet.setName(componentNameSetDto.name());
        componentNameSetRepository.save(componentNameSet);
    }

    @Transactional
    public void deleteComponent(Long id) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор компонентов не найден"));
        componentNameSetHistoryService.addComponentNameSetHistory(componentNameSet.getName(), componentNameSet.getName(), false, 3);
        componentNameSetRepository.delete(componentNameSet);
    }
}
