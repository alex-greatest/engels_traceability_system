package com.rena.application.service.boiler_type;

import com.rena.application.config.mapper.BoilerTypeMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeDto;
import com.rena.application.entity.model.boiler.BoilerType;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler_type.BoilerTypeRepository;
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
public class BoilerTypeService {
    private final BoilerTypeRepository boilerTypeRepository;
    private final BoilerTypeMapper boilerTypeMapper;
    private final BoilerTypeHistoryService boilerTypeHistoryService;
    private final ComponentNameSetRepository componentNameSetRepository;

    public List<BoilerTypeDto> getAllBoilers() {
        List<BoilerType> boilerTypes = boilerTypeRepository.findAllBoilers();
        return boilerTypeMapper.toBoilerDto(boilerTypes);
    }

    @Transactional
    public void addBoiler(BoilerTypeDto boilerTypeDto) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(boilerTypeDto.componentNameSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        BoilerType boilerType = boilerTypeMapper.toEntity(boilerTypeDto);
        boilerType.setComponentNameSet(componentNameSet);
        boilerTypeRepository.save(boilerType);
        boilerTypeHistoryService.addBoilerHistory(boilerType.getId(), null, boilerType.getComponentNameSet().getName(),
                boilerType.getModel(), boilerType.getTypeName(), true, 1);
    }

    @Transactional
    public void updateBoiler(BoilerTypeDto boilerTypeDto) {
        BoilerType boilerType = boilerTypeRepository.findBoilerById(boilerTypeDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        ComponentNameSet componentNameSetNew = componentNameSetRepository.findById(boilerTypeDto.componentNameSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        String oldTypeName = boilerType.getTypeName();
        boilerTypeHistoryService.addBoilerHistory(boilerType.getId(), oldTypeName, componentNameSetNew.getName(),
                boilerTypeDto.model(), boilerTypeDto.typeName(), true, 2);
        boilerType.setComponentNameSet(componentNameSetNew);
        boilerType.setModel(boilerTypeDto.model());
        boilerType.setTypeName(boilerTypeDto.typeName());
        boilerTypeRepository.save(boilerType);
    }

    @Transactional
    public void deleteComponent(Long id) {
        BoilerType boilerType = boilerTypeRepository.findBoilerById(id).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        boilerTypeHistoryService.addBoilerHistory(boilerType.getId(), null,
                boilerType.getComponentNameSet().getName(), boilerType.getModel(), boilerType.getTypeName(), false, 3);
        boilerTypeRepository.delete(boilerType);
    }
}
