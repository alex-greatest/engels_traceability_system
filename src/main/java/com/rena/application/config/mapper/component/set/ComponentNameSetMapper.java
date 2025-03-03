package com.rena.application.config.mapper.component.set;

import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.rena.application.entity.model.component.set.ComponentNameSet;
import org.mapstruct.*;
import java.util.Collection;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentNameSetMapper {
    ComponentNameSet toEntity(ComponentNameSetDto componentNameSetDto);

    ComponentNameSetDto toDto(ComponentNameSet componentNameSet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ComponentNameSet partialUpdate(ComponentNameSetDto componentNameSetDto, @MappingTarget ComponentNameSet componentNameSet);

    List<ComponentNameSetDto> toDto(Collection<ComponentNameSet> componentNameSet);
}