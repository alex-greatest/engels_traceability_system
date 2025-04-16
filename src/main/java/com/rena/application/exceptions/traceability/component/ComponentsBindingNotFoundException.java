package com.rena.application.exceptions.traceability.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentsBindingNotFoundException extends EndpointException {
    public ComponentsBindingNotFoundException(String message) {
        super(message);
    }
}
