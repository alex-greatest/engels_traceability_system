package com.rena.application.config.mapper.component.set;

import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentSetOperation;
import com.rena.application.entity.model.settings.component.set.ComponentSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentSetMapper {
    @Mapping(source = "componentTypeName", target = "componentType.name")
    ComponentSet toEntity(ComponentSetOperation componentSetOperation);

    @Mapping(source = "componentType.name", target = "componentTypeName")
    ComponentSetOperation toComponentSetOperation(ComponentSet componentSet);

    List<ComponentSetOperation> toComponentSetOperation(List<ComponentSet> componentSet);
}