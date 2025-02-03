package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.mapper.BoilerTypeAdditionalDataSetMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalDataSetDto;
import com.rena.application.entity.model.boiler.BoilerTypeAdditionalDataSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalValueRepository;
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
public class BoilerTypeAdditionalValueService {
    private final BoilerTypeAdditionalValueRepository boilerTypeAdditionalValueRepository;

    private final BoilerTypeAdditionalDataSetRepository boilerTypeAdditionalDataSetRepository;
    private final BoilerTypeAdditionalDataSetHistoryService boilerTypeAdditionalDataSetHistoryService;
    private final BoilerTypeAdditionalDataSetMapper boilerTypeAdditionalDataSetMapper;

    public List<BoilerTypeAdditionalDataSetDto> getAllDataSet(Long boilerAdditionalDataSet) {
        /*var boilerTypeAdditionalValues = boilerTypeAdditionalValueRepository.
                findByBoilerTypeAdditionalData_IdAndBoilerType_IdAndBoilerTypeAdditionalDataSet_Id();*/



        List<BoilerTypeAdditionalDataSet> componentNameSets = boilerTypeAdditionalDataSetRepository.findAll();
        return boilerTypeAdditionalDataSetMapper.toDto(componentNameSets);
    }

    public BoilerTypeAdditionalDataSetDto getDataSet(Long id)
    {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        return boilerTypeAdditionalDataSetMapper.toBoilerTypeAdditionalDataSetDto(boilerTypeAdditionalDataSet);
    }

    @Transactional
    public void addDataSet(BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetMapper
                .toEntity(boilerTypeAdditionalDataSetDto);
        boilerTypeAdditionalDataSetRepository.save(boilerTypeAdditionalDataSet);
        boilerTypeAdditionalDataSetHistoryService.addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(),
                null, boilerTypeAdditionalDataSet.getName(), true, 1);
    }

    @Transactional
    public void updateDataSet(BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.
                findById(boilerTypeAdditionalDataSetDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        String oldNameNameSet = boilerTypeAdditionalDataSet.getName();
        boilerTypeAdditionalDataSetHistoryService.addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(),
                oldNameNameSet, boilerTypeAdditionalDataSetDto.name(), true, 2);
        boilerTypeAdditionalDataSet.setName(boilerTypeAdditionalDataSetDto.name());
        boilerTypeAdditionalDataSetRepository.save(boilerTypeAdditionalDataSet);
    }

    @Transactional
    public void deleteComponent(Long id) {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        boilerTypeAdditionalDataSetHistoryService.addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(),
                null, boilerTypeAdditionalDataSet.getName(), false, 3);
        boilerTypeAdditionalDataSetRepository.delete(boilerTypeAdditionalDataSet);
    }
}
