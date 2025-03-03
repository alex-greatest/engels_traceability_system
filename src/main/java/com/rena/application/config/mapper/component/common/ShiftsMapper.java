package com.rena.application.config.mapper.component.common;

import com.rena.application.entity.dto.shift.ShiftDto;
import com.rena.application.entity.model.shift.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShiftsMapper {
    Shift toEntity(ShiftDto shiftDto);

    ShiftDto toShiftsDto(Shift shift);

    List<ShiftDto> toDto(List<Shift> shifts);
}