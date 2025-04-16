package com.rena.application.exceptions.traceability.boiler;

import com.vaadin.hilla.exception.EndpointException;

public class BoilerNotFoundException extends EndpointException {
    public BoilerNotFoundException(String message) {
        super(message);
    }
}
