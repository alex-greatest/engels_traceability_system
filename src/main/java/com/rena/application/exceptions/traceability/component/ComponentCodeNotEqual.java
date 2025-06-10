package com.rena.application.exceptions.traceability.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentCodeNotEqual extends EndpointException {
    public ComponentCodeNotEqual(String message) {
        super(message);
    }
}
