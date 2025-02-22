package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;

public class BoilerOrderNotFoundException extends EndpointException {
    public BoilerOrderNotFoundException(String message) {
        super(message);
    }
}
