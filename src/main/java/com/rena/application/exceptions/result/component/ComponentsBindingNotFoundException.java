package com.rena.application.exceptions.result.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentsBindingNotFoundException extends EndpointException {
    public ComponentsBindingNotFoundException(String message) {
        super(message);
    }
}
