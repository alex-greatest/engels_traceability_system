package com.rena.application.config.mapper.result;

import com.rena.application.entity.dto.print.OperationResult;
import com.rena.application.entity.model.traceability.common.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(target = "stationName", source = "station.name")
    @Mapping(target = "status", source = "status.name")
    OperationResult toDto(Operation operation);

    List<OperationResult> toDto(List<Operation> operation);
}
