package com.rena.application.entity.dto.print;

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
