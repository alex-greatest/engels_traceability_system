package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class BoilerTraceabilityPrevNOK extends EndpointException {
    public BoilerTraceabilityPrevNOK(String message) {
        super(message);
    }
}
