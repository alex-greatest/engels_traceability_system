package com.rena.application.service.traceability.helper;

import com.rena.application.entity.dto.traceability.common.exchange.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErrorHelper {
    public ErrorResponse getErrorResponse(String message, String correlationId) {
        var errorResponse = new ErrorResponse(message);
        errorResponse.setCorrelationId(correlationId);
        return errorResponse;
    }
}
