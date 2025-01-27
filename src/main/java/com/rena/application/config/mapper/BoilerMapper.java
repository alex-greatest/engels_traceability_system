package com.rena.application.config.mapper;

import com.rena.application.entity.dto.boiler.BoilerDto;
import com.rena.application.entity.model.boiler.Boiler;
import org.mapstruct.*;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerMapper {
    Boiler toEntity(BoilerDto boilerDto);

    BoilerDto toDto(Boiler boiler);

    List<BoilerDto> toBoilerDto(List<Boiler> boiler);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Boiler partialUpdate(BoilerDto boilerDto, @MappingTarget Boiler boiler);
}