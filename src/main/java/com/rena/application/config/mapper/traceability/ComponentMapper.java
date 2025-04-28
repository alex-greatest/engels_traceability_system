package com.rena.application.config.mapper.traceability;

import com.rena.application.entity.dto.result.ComponentResult;
import com.rena.application.entity.model.traceability.station.component.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {
    Component toEntity(ComponentResult componentResult);

    List<ComponentResult> toComponentResult(List<Component> component);

    ComponentResult toComponentResult(Component component);

    List<Component> toEntity(List<ComponentResult> componentResult);
}