package com.rena.application.config.mapper.traceability;

import com.rena.application.entity.dto.result.OperationResult;
import com.rena.application.entity.model.traceability.common.station.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(target = "stationName", source = "station.name")
    OperationResult toDto(Operation operation);

    List<OperationResult> toDto(List<Operation> operation);
}
