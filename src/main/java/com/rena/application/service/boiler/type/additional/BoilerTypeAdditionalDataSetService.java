package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.mapper.type.BoilerTypeAdditionalDataSetMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalDataSetDto;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalDataSet;
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
@Transactional
@Slf4j
public class BoilerTypeAdditionalDataSetService {
    private final BoilerTypeAdditionalDataSetRepository boilerTypeAdditionalDataSetRepository;
    private final BoilerTypeAdditionalDataSetMapper boilerTypeAdditionalDataSetMapper;
    private final BoilerTypeAdditionalValueRepository boilerTypeAdditionalValueRepository;

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
        boilerTypeAdditionalValueRepository.addAdditionalValue(boilerTypeAdditionalDataSet.getId());
    }

    @Transactional
    public void updateDataSet(BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.
                findById(boilerTypeAdditionalDataSetDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        boilerTypeAdditionalDataSet.setName(boilerTypeAdditionalDataSetDto.name());
        boilerTypeAdditionalDataSetRepository.save(boilerTypeAdditionalDataSet);
    }

    @Transactional
    public void deleteDataSet(Long id) {
        var boilerTypeAdditionalDataSet = boilerTypeAdditionalDataSetRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException("Набор данных котла не найден"));
        boilerTypeAdditionalDataSetRepository.delete(boilerTypeAdditionalDataSet);
    }
}
