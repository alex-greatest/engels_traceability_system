package com.rena.application.config.mapper;

import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalValueDto;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalValue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BoilerTypeAdditionalDataSetMapper.class})
public interface BoilerTypeAdditionalValueMapper {
    BoilerTypeAdditionalValue toEntity(BoilerTypeAdditionalValueDto boilerTypeAdditionalValueDto);

    BoilerTypeAdditionalValueDto toBoilerTypeAdditionalValueDto(BoilerTypeAdditionalValue boilerTypeAdditionalValue);

    List<BoilerTypeAdditionalValueDto> toDto(List<BoilerTypeAdditionalValue> boilerTypeAdditionalValue);
}