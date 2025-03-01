package com.rena.application.service.component;

import com.rena.application.config.mapper.ComponentBindingMapper;
import com.rena.application.entity.dto.component.ComponentBindingDto;
import com.rena.application.entity.model.component.ComponentBinding;
import com.rena.application.repository.component.ComponentBindingRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ComponentBindingService {
    private final ComponentBindingRepository componentBindingRepository;
    private final ComponentBindingMapper componentBindingMapper;

    public List<ComponentBindingDto> getAllComponentsBinding(String stationName) {
        List<ComponentBinding> componentNameSets = componentBindingRepository.findByStation_Name(stationName);
        return componentBindingMapper.toDto(componentNameSets);
    }
}
