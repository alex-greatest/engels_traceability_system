package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.mapper.BoilerTypeAdditionalDataSetMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalDataSetDto;
import com.rena.application.entity.model.boiler.BoilerTypeAdditionalDataSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetHistoryRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalDataSetRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalValueHistoryRepository;
import com.rena.application.repository.boiler.type.additional.BoilerTypeAdditionalValueRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class BoilerTypeAdditionalDataSetService {
    private final BoilerTypeAdditionalDataSetRepository boilerTypeAdditionalDataSetRepository;
    private final BoilerTypeAdditionalDataSetHistoryService boilerTypeAdditionalDataSetHistoryService;
    private final BoilerTypeAdditionalDataSetMapper boilerTypeAdditionalDataSetMapper;
    private final BoilerTypeAdditionalValueRepository boilerTypeAdditionalValueRepository;
    private final BoilerTypeAdditionalValueHistoryRepository boilerTypeAdditionalValueHistoryRepository;
    private final BoilerTypeAdditionalDataSetHistoryRepository boilerTypeAdditionalDataSetHistoryRepository;

    public List<BoilerTypeAdditionalDataSetDto> getAllDataSet() {
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
        var boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetMapper
                .toEntity(boilerTypeAdditionalDataSetDto);
        boilerTypeAdditionalDataSetRepository.save(boilerTypeAdditionalDataSet);
        var boilerTypeAdditionalDataSetHistory = boilerTypeAdditionalDataSetHistoryService.
                addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(), null,
                        boilerTypeAdditionalDataSet.getName(), true, 1);
        boilerTypeAdditionalValueRepository.addAdditionalValue(boilerTypeAdditionalDataSet.getId());
        boilerTypeAdditionalValueHistoryRepository.
                addAdditionalValueHistory(boilerTypeAdditionalDataSetHistory.getId(),
                        boilerTypeAdditionalDataSetHistory.getUserHistory().getId(),
                        boilerTypeAdditionalDataSet.getId(), LocalDateTime.now());
    }

    @Transactional
    public void updateDataSet(BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.
                findById(boilerTypeAdditionalDataSetDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        var oldBoilerTypeAdditionalDataSetHistory = boilerTypeAdditionalDataSetHistoryRepository.
                findByBoilerTypeAdditionDataSetIdAndIsActive(boilerTypeAdditionalDataSetDto.id(), true).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        String oldNameNameSet = boilerTypeAdditionalDataSet.getName();
        var newBoilerTypeAdditionalDataSetHistory = boilerTypeAdditionalDataSetHistoryService.
                addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(),
                oldNameNameSet, boilerTypeAdditionalDataSetDto.name(), true, 2);
        boilerTypeAdditionalDataSet.setName(boilerTypeAdditionalDataSetDto.name());
        boilerTypeAdditionalDataSetRepository.save(boilerTypeAdditionalDataSet);
        boilerTypeAdditionalValueHistoryRepository.updateDataSetId(oldBoilerTypeAdditionalDataSetHistory.getId(),
                newBoilerTypeAdditionalDataSetHistory.getId());
    }

    @Transactional
    public void deleteDataSet(Long id) {
        var boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        var oldBoilerTypeAdditionalDataSetHistory = boilerTypeAdditionalDataSetHistoryRepository.
                findByBoilerTypeAdditionDataSetIdAndIsActive(id, true).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        var newBoilerTypeAdditionalDataSetHistory = boilerTypeAdditionalDataSetHistoryService.
                addBoilerTypeAdditionDataNameSetHistory(boilerTypeAdditionalDataSet.getId(),
                null, boilerTypeAdditionalDataSet.getName(), false, 3);
        boilerTypeAdditionalDataSetRepository.delete(boilerTypeAdditionalDataSet);
        boilerTypeAdditionalValueHistoryRepository.
                deleteBoilerTypeAdditionalValueHistories(oldBoilerTypeAdditionalDataSetHistory.getId(),
                        newBoilerTypeAdditionalDataSetHistory.getId());
    }
}
