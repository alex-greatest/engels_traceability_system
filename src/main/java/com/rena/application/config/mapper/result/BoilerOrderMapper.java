package com.rena.application.config.mapper.result;

import com.rena.application.entity.dto.result.print.BoilerOrderDto;
import com.rena.application.entity.model.result.station.wp.one.order.BoilerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerOrderMapper {
    BoilerOrder toEntity(BoilerOrderDto boilerOrderDto);

    BoilerOrderDto toBoilerOrderDto(BoilerOrder boilerOrder);

    List<BoilerOrderDto> toDto(List<BoilerOrder> boilerOrder);
}