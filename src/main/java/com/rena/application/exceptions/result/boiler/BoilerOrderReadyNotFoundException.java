package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;

public class BoilerOrderReadyNotFoundException extends EndpointException {
    public BoilerOrderReadyNotFoundException(String message) {
        super(message);
    }
}
