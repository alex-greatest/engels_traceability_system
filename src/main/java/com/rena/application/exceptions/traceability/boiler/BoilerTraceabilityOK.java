package com.rena.application.exceptions.traceability.boiler;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class BoilerTraceabilityOK extends EndpointException {
    public BoilerTraceabilityOK(String message) {
        super(message);
    }
}
