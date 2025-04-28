package com.rena.application.entity.dto.result;

import java.time.LocalDateTime;

/**
 * DTO for Operation
 */
public record OperationResult(
    Long id,
    String stationName,
    String status,
    LocalDateTime dateCreate,
    Boolean isLast,
    String stationDescription
) {}
