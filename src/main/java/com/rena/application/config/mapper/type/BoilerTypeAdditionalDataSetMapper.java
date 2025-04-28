package com.rena.application.config.mapper.type;

import com.rena.application.entity.dto.settings.boiler_type.BoilerTypeAdditionalDataSetDto;
import com.rena.application.entity.model.settings.type.additional.BoilerTypeAdditionalDataSet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerTypeAdditionalDataSetMapper {
    BoilerTypeAdditionalDataSet toEntity(BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto);

    BoilerTypeAdditionalDataSetDto toBoilerTypeAdditionalDataSetDto(BoilerTypeAdditionalDataSet boilerTypeAdditionalDataSet);

    List<BoilerTypeAdditionalDataSetDto> toDto(List<BoilerTypeAdditionalDataSet> boilerTypeAdditionalDataSet);
}