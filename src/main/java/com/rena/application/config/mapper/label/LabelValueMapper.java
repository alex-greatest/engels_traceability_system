package com.rena.application.config.mapper.label;

import com.rena.application.entity.dto.traceability.label.LabelValueDto;
import com.rena.application.entity.model.traceability.label.LabelValue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LabelValueMapper {
    LabelValue toEntity(LabelValueDto labelValueDto);

    LabelValueDto toLabelValueDto(LabelValue labelValue);

    List<LabelValueDto> toLabelValueDto(List<LabelValue> labelValue);
}