package com.rena.application.service.component;

import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.model.component.Component;
import com.rena.application.repository.component.ComponentSetHistoryRepository;
import com.rena.application.repository.component.ComponentSetRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ComponentSetService {
    private final ComponentSetRepository componentRepository;
    private final ComponentSetHistoryRepository componentHistoryService;

    public List<ComponentSetDto> getAllComponents() {
        List<Component> component = componentRepository.findAll();
        return componentMapper.toDto(component);
    }
}
