package com.rena.application.config.mapper;

import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.model.component.ComponentSet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentSetMapper {
    ComponentSet toEntity(ComponentSetDto componentSetDto);

    ComponentSetDto toComponentSetDto(ComponentSet componentSet);

    List<ComponentSetDto> toComponentSetDto(List<ComponentSet> componentSet);
}