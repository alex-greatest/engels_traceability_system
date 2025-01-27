package com.rena.application.config.mapper;

import com.rena.application.entity.dto.boiler_type.BoilerTypeDto;
import com.rena.application.entity.model.boiler.BoilerType;
import org.mapstruct.*;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerTypeMapper {
    BoilerType toEntity(BoilerTypeDto boilerTypeDto);

    BoilerTypeDto toDto(BoilerType boilerType);

    List<BoilerTypeDto> toBoilerDto(List<BoilerType> boilerType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BoilerType partialUpdate(BoilerTypeDto boilerTypeDto, @MappingTarget BoilerType boilerType);
}