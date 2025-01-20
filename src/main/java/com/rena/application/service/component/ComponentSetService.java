package com.rena.application.service.component;

import com.rena.application.config.mapper.ComponentSetMapper;
import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.dto.component.ComponentSetDtos;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.entity.model.component.ComponentSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.component.ComponentNameSetRepository;
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
    private final ComponentSetRepository componentSetRepository;
    private final ComponentSetHistoryRepository componentSetHistoryService;
    private final ComponentNameSetRepository componentNameSetRepository;
    private final ComponentSetMapper componentSetMapper;

    public ComponentSetDtos getAllComponents(Long id) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException("Не удалось найти набор компонентов"));
        List<ComponentSet> componentSet = componentSetRepository.findAllComponentsById(id);
        List<ComponentSetDto> componentSetDtoList = componentSetMapper.toComponentSetDto(componentSet);
        return new ComponentSetDtos(componentNameSet.getName(), componentSetDtoList);
    }
}
