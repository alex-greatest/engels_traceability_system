package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;

public class BoilerNotFoundException extends EndpointException {
    public BoilerNotFoundException(String message) {
        super(message);
    }
}
