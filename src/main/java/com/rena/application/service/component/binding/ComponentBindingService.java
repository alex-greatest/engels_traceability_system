package com.rena.application.service.component.binding;

import com.rena.application.config.mapper.component.binding.ComponentBindingMapper;
import com.rena.application.entity.dto.component.ComponentBindingRequest;
import com.rena.application.entity.dto.component.ComponentBindingResponse;
import com.rena.application.entity.model.component.ComponentType;
import com.rena.application.entity.model.component.binding.ComponentBinding;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.binding.ComponentBindingRepository;
import com.rena.application.repository.component.set.ComponentNameSetRepository;
import com.rena.application.repository.component.set.ComponentTypeRepository;
import com.rena.application.repository.result.common.StationRepository;
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
public class ComponentBindingService {
    private final ComponentBindingRepository componentBindingRepository;
    private final ComponentBindingMapper componentBindingMapper;
    private final ComponentNameSetRepository componentNameSetRepository;
    private final ComponentTypeRepository componentTypeRepository;
    private final StationRepository stationRepository;

    public List<ComponentBindingResponse> getAllComponentsBinding(String stationName, Long idNameSet) {
        var componentNameSets = componentBindingRepository.findByStation_Name(stationName, idNameSet);
        return componentBindingMapper.toDto(componentNameSets);
    }

    @Transactional
    public void addComponentBinding(ComponentBindingRequest componentBindingRequest) {
        var componentNameSet = componentNameSetRepository.
                findById(componentBindingRequest.componentNameSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набор компонентов не найден"));
        var componentType = componentTypeRepository.
                findComponentTypeByName(componentBindingRequest.componentType().name()).
                orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
        var station = stationRepository.
                findByName(componentBindingRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var componentBinding = new ComponentBinding();
        componentBinding.setComponentType(componentType);
        componentBinding.setComponentNameSet(componentNameSet);
        componentBinding.setStation(station);
        componentBinding.setOrder(componentBindingRequest.order());
        componentBindingRepository.save(componentBinding);
    }

    @Transactional
    public void updateComponentBinding(List<ComponentBindingRequest> componentsBindingRequest) {
        componentsBindingRequest.forEach(componentBindingRequest -> {
            var componentBinding = componentBindingRepository.
                    findBindingById(componentBindingRequest.id())
                    .orElseThrow(() -> new RecordNotFoundException("Привязка не найдена"));
            var componentType = componentTypeRepository.
                    findComponentTypeByName(componentBindingRequest.componentType().name()).
                    orElseThrow(() -> new RecordNotFoundException("Тип компонента не найден"));
            componentBinding.setComponentType(componentType);
            componentBinding.setOrder(componentBindingRequest.order());
            componentBindingRepository.save(componentBinding);
        });
    }

    @Transactional
    public void deleteComponentBinding(Long id) {
        var componentBinding = componentBindingRepository.
                findBindingById(id).orElseThrow(() -> new RecordNotFoundException("Привязка не найдена"));
        componentBindingRepository.delete(componentBinding);
    }

    public void deleteComponentBinding(ComponentType componentType) {
        componentBindingRepository.deleteByComponentType(componentType);
    }
}
