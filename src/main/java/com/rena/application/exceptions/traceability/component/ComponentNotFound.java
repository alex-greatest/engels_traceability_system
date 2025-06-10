package com.rena.application.exceptions.traceability.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentNotFound extends EndpointException {
    public ComponentNotFound(String message) {
        super(message);
    }
}
