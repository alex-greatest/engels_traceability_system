package com.rena.application.config.mapper.component.binding;

import com.rena.application.entity.dto.component.ComponentNameBindingDto;
import com.rena.application.entity.model.component.binding.ComponentNameBinding;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentNameBindingMapper {
    ComponentNameBinding toEntity(ComponentNameBindingDto componentNameBindingDto);

    ComponentNameBindingDto toComponentNameBindingDto(ComponentNameBinding componentNameBinding);

    List<ComponentNameBindingDto> toDto(List<ComponentNameBinding> componentNameBinding);
}