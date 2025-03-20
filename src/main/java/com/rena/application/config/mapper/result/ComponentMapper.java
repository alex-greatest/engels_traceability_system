package com.rena.application.config.mapper.result;

import com.rena.application.entity.dto.result.print.ComponentResult;
import com.rena.application.entity.model.result.station.wp.two.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {
    Component toEntity(ComponentResult componentResult);

    ComponentResult toComponentResult(Component component);

    List<ComponentResult> toDto(List<Component> component);
}