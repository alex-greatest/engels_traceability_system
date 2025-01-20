package com.rena.application.config.mapper;

import com.rena.application.entity.dto.component.ComponentDto;
import com.rena.application.entity.model.component.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {
    Component toEntity(ComponentDto componentDto);

    ComponentDto toComponentDto(Component component);

    List<ComponentDto> toDto(List<Component> component);
}