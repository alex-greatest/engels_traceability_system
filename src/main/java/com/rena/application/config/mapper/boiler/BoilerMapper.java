package com.rena.application.config.mapper.boiler;

import com.rena.application.entity.dto.result.BoilerResult;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryResponse;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoilerMapper {
    Boiler toEntity(BoilerHistoryResponse boilerHistoryResponse);

    BoilerHistoryResponse toBoilerResponse(Boiler boiler);

    List<BoilerHistoryResponse> toDto(List<Boiler> boiler);

    Boiler toEntity(BoilerResult boilerResult);

    BoilerResult toBoilerResult(Boiler boiler);

    List<BoilerResult> toDtoBoilerResult(List<Boiler> boiler);
}