package com.rena.application.entity.dto.result.print;

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
