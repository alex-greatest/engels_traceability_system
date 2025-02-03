package com.rena.application.service.boiler.type.additional;

import com.rena.application.config.mapper.BoilerTypeAdditionalValueMapper;
import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalValueDto;
import com.rena.application.entity.model.boiler.BoilerTypeAdditionalValue;
import com.rena.application.exceptions.RecordNotFoundException;
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
    private final BoilerTypeAdditionalValueMapper boilerTypeAdditionalValueMapper;
    private final BoilerTypeAdditionalValueHistoryService boilerTypeAdditionalValueHistoryService;

    public List<BoilerTypeAdditionalValueDto> getAllValue(Long boilerAdditionalDataSet) {
        List<BoilerTypeAdditionalValue> boilerTypeAdditionalValues = boilerTypeAdditionalValueRepository.
                findAllBoilerTypeAdditionalValue(boilerAdditionalDataSet);
        return boilerTypeAdditionalValueMapper.toDto(boilerTypeAdditionalValues);
    }

    @Transactional
    public void updateAdditionalValue(List<BoilerTypeAdditionalValueDto> boilerTypeAdditionalValueDtos) {
        boilerTypeAdditionalValueDtos.forEach(boilerTypeAdditionalValueDto -> {
            var boilerAdditionalValue = boilerTypeAdditionalValueRepository.
                    findBoilerTypeAdditionalValueById(boilerTypeAdditionalValueDto.id()).
                    orElseThrow(() -> new RecordNotFoundException("Значение не найдено"));
            String oldValue = boilerAdditionalValue.getValue();
            boilerAdditionalValue.setValue(boilerTypeAdditionalValueDto.value());
            boilerAdditionalValue.setUnit(boilerTypeAdditionalValueDto.unit());
            boilerTypeAdditionalValueHistoryService.addBoilerTypeAdditionDataValueHistory(
                    boilerAdditionalValue.getBoilerTypeAdditionalDataSet().getId(),
                    boilerAdditionalValue.getBoilerTypeAdditionalData().getId(),
                    oldValue, boilerTypeAdditionalValueDto.value(), boilerTypeAdditionalValueDto.unit(),
                    true, 2);
        });
    }
}
