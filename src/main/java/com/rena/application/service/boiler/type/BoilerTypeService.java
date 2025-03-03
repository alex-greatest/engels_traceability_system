package com.rena.application.service.boiler.type;

import com.rena.application.config.mapper.BoilerTypeMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeDto;
import com.rena.application.entity.model.boiler.type.BoilerType;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSet;
import com.rena.application.entity.model.component.set.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetRepository;
import com.rena.application.repository.component.set.ComponentNameSetRepository;
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
    private final BoilerCycleService boilerCycleService;
    private final BoilerTypeAdditionalDataSetRepository boilerTypeAdditionalDataSetRepository;
    private final ComponentNameSetRepository componentNameSetRepository;

    public List<BoilerTypeDto> getAllBoilers() {
        List<BoilerType> boilerTypes = boilerTypeRepository.findAllBoilers();
        return boilerTypeMapper.toBoilerDto(boilerTypes);
    }

    @Transactional
    public void addBoiler(BoilerTypeDto boilerTypeDto) {
        ComponentNameSet componentNameSet = componentNameSetRepository.findById(boilerTypeDto.componentNameSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.
                findById(boilerTypeDto.boilerTypeAdditionalDataSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        BoilerType boilerType = boilerTypeMapper.toEntity(boilerTypeDto);
        boilerType.setComponentNameSet(componentNameSet);
        boilerType.setBoilerTypeAdditionalDataSet(boilerTypeAdditionalDataSet);
        boilerTypeRepository.save(boilerType);
        boilerCycleService.addBoilerCycle(boilerType.getId(), boilerType.getArticle(), boilerType.getModel(),
                boilerType.getTypeName(), 1, true);
    }

    @Transactional
    public void updateBoiler(BoilerTypeDto boilerTypeDto) {
        BoilerType boilerType = boilerTypeRepository.findBoilerById(boilerTypeDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Тип котла не найден"));
        ComponentNameSet componentNameSetNew = componentNameSetRepository.findById(boilerTypeDto.componentNameSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.
                findById(boilerTypeDto.boilerTypeAdditionalDataSet().id()).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        boilerType.setComponentNameSet(componentNameSetNew);
        boilerType.setModel(boilerTypeDto.model());
        boilerType.setTypeName(boilerTypeDto.typeName());
        boilerType.setArticle(boilerType.getArticle());
        boilerType.setBoilerTypeAdditionalDataSet(boilerTypeAdditionalDataSet);
        boilerTypeRepository.save(boilerType);
        boilerCycleService.addBoilerCycle(boilerType.getId(), boilerType.getArticle(), boilerType.getModel(),
                boilerType.getTypeName(), 2, true);
    }

    @Transactional
    public void deleteComponent(Long id) {
        BoilerType boilerType = boilerTypeRepository.findBoilerById(id).
                orElseThrow(() -> new RecordNotFoundException("Тип котла не найден"));
        boilerTypeRepository.delete(boilerType);
        boilerCycleService.addBoilerCycle(boilerType.getId(), boilerType.getArticle(), boilerType.getModel(),
                boilerType.getTypeName(), 3, false);
    }
}
