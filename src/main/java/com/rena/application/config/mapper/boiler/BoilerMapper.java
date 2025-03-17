package com.rena.application.config.mapper.boiler;

import com.rena.application.entity.dto.result.print.BoilerResult;
import com.rena.application.entity.dto.result.station.wp.one.boiler.BoilerResponseWpOne;
import com.rena.application.entity.model.result.common.Boiler;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerMapper {
    Boiler toEntity(BoilerResponseWpOne boilerResponseWpOne);

    BoilerResponseWpOne toBoilerResponse(Boiler boiler);

    List<BoilerResponseWpOne> toDto(List<Boiler> boiler);

    Boiler toEntity(BoilerResult boilerResult);

    BoilerResult toBoilerResult(Boiler boiler);

    List<BoilerResult> toDtoBoilerResult(List<Boiler> boiler);
}