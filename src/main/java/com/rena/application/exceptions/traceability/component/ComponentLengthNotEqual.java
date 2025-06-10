package com.rena.application.exceptions.traceability.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentLengthNotEqual extends EndpointException {
    public ComponentLengthNotEqual(String message) {
        super(message);
    }
}
