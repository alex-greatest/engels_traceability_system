package com.rena.application.config.mapper.component;

import com.rena.application.entity.dto.component.ComponentTypeDto;
import com.rena.application.entity.model.component.ComponentType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentTypeMapper {
    ComponentType toEntity(ComponentTypeDto componentTypeDto);

    ComponentTypeDto toComponentDto(ComponentType componentType);

    List<ComponentTypeDto> toDto(List<ComponentType> componentType);
}