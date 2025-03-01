package com.rena.application.config.mapper;

import com.rena.application.entity.dto.component.ComponentBindingDto;
import com.rena.application.entity.model.component.ComponentBinding;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentBindingMapper {
    ComponentBinding toEntity(ComponentBindingDto componentBindingDto);

    ComponentBindingDto toComponentBindingDto(ComponentBinding componentBinding);

    List<ComponentBindingDto> toDto(List<ComponentBinding> componentBinding);
}