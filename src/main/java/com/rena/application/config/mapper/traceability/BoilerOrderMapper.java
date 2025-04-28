package com.rena.application.config.mapper.traceability;

import com.rena.application.entity.dto.result.BoilerOrderDto;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerOrderMapper {
    List<BoilerOrderDto> toBoilerOrderDto(List<BoilerOrder> boilerOrder);

    BoilerOrderDto toBoilerOrderDto(BoilerOrder boilerOrder);

    List<BoilerOrder> toEntity(List<BoilerOrderDto> boilerOrderDto);

    BoilerOrder toEntity(BoilerOrderDto boilerOrderDto);
}