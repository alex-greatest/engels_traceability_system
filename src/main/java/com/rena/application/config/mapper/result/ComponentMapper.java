package com.rena.application.config.mapper.result;

import com.rena.application.entity.dto.result.print.ComponentDto;
import com.rena.application.entity.model.result.station.wp.two.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {
    Component toEntity(ComponentDto componentDto);

    ComponentDto toComponentDto(Component component);
}