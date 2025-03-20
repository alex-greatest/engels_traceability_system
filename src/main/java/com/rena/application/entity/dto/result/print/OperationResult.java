package com.rena.application.entity.dto.result.print;

import java.time.LocalDateTime;

public record OperationResult(
    Long id,
    String stationName,
    String status,
    LocalDateTime dateCreate,
    Boolean isLast,
    String stationDescription
) {}
