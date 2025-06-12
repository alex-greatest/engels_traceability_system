package com.rena.application.config.mapper.traceability;

import com.rena.application.entity.dto.traceability.station.component.result.ComponentResultRequest;
import com.rena.application.entity.model.traceability.station.component.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {
    Component toEntity(ComponentResultRequest componentResultRequest);

    List<ComponentResultRequest> toComponentResult(List<Component> component);

    ComponentResultRequest toComponentResult(Component component);

    List<Component> toEntity(List<ComponentResultRequest> componentResultRequest);
}