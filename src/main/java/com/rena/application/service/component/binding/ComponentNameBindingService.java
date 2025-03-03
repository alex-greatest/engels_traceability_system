package com.rena.application.service.component.binding;

import com.rena.application.config.mapper.component.binding.ComponentNameBindingMapper;
import com.rena.application.entity.dto.component.ComponentNameBindingDto;
import com.rena.application.entity.model.component.binding.ComponentNameBinding;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.binding.ComponentNameBindingRepository;
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
public class ComponentNameBindingService {
    private final ComponentNameBindingRepository componentNameBindingRepository;
    private final ComponentNameBindingMapper componentNameBindingMapper;

    public List<ComponentNameBindingDto> getAllComponentsNameBinding() {
        List<ComponentNameBinding> componentNameSets = componentNameBindingRepository.findAll();
        return componentNameBindingMapper.toDto(componentNameSets);
    }

    public ComponentNameBindingDto getComponentNameBinding(Long id)
    {
        ComponentNameBinding componentNameBinding = componentNameBindingRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Название набора привязок компонентов не найдено"));
        return componentNameBindingMapper.toComponentNameBindingDto(componentNameBinding);
    }

    @Transactional
    public void addComponentBindingName(ComponentNameBindingDto componentNameBindingDto) {
        ComponentNameBinding componentNameBinding = componentNameBindingMapper.toEntity(componentNameBindingDto);
        componentNameBindingRepository.save(componentNameBinding);
    }

    @Transactional
    public void updateComponentBindingName(Long id, ComponentNameBindingDto componentNameBindingDto) {
        ComponentNameBinding componentNameBinding = componentNameBindingRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Название набора привязок компонентов не найдено"));
        componentNameBinding.setName(componentNameBindingDto.name());
        componentNameBindingRepository.save(componentNameBinding);
    }

    @Transactional
    public void deleteComponentBindingName(Long id) {
        ComponentNameBinding componentNameBinding = componentNameBindingRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Название набора привязок компонентов не найдено"));
        componentNameBindingRepository.delete(componentNameBinding);
    }
}
