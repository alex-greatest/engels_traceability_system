package com.rena.application.config.mapper.component.binding;

import com.rena.application.config.mapper.component.ComponentTypeMapper;
import com.rena.application.config.mapper.component.set.ComponentNameSetMapper;
import com.rena.application.entity.dto.component.ComponentBindingRequest;
import com.rena.application.entity.dto.component.ComponentBindingResponse;
import com.rena.application.entity.model.component.binding.ComponentBinding;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ComponentTypeMapper.class, ComponentNameSetMapper.class})
public interface ComponentBindingMapper {
    ComponentBinding toEntity(ComponentBindingResponse componentBindingResponse);

    ComponentBindingResponse toComponentBindingDto(ComponentBinding componentBinding);

    List<ComponentBindingResponse> toDto(List<ComponentBinding> componentBinding);

    ComponentBinding toEntity(ComponentBindingRequest componentBindingRequest);

    ComponentBindingRequest toComponentBindingRequest(ComponentBinding componentBinding);

    ComponentBindingRequest toComponentBindingDto1(ComponentBinding componentBinding);
}