package com.rena.application.service.traceability.helper;

import com.rena.application.entity.dto.traceability.common.exchange.ErrorResponse;
import com.rena.application.entity.dto.traceability.common.router.ErrorRoute;
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

    public ErrorRoute getErrorRoute(String serialNumber, String message, String correlationId) {
        var errorRouter = new ErrorRoute(serialNumber, message);
        errorRouter.setCorrelationId(correlationId);
        return errorRouter;
    }
}
