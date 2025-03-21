package com.rena.application.service.component.set;

import com.rena.application.config.mapper.component.set.ComponentNameSetMapper;
import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.rena.application.entity.model.component.set.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.set.ComponentNameSetRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ComponentNameSetService {
    private final ComponentNameSetRepository componentNameSetRepository;
    private final ComponentNameSetMapper componentNameSetMapper;

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
        componentNameSetRepository.save(componentNameSet);
    }

    @Transactional
    public void updateComponent(Long id, ComponentNameSetDto componentNameSetDto) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        componentNameSet.setName(componentNameSetDto.name());
        componentNameSetRepository.save(componentNameSet);
    }

    @Transactional
    public void deleteComponent(Long id) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор компонентов не найден"));
        componentNameSetRepository.delete(componentNameSet);
    }
}
