package com.rena.application.config.mapper.result;

import com.rena.application.entity.dto.print.StatusDto;
import com.rena.application.entity.model.traceability.common.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusMapper {
    Status toEntity(StatusDto statusDto);

    StatusDto toStatusDto(Status status);
}