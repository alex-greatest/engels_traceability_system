package com.rena.application.exceptions.traceability.component;

import com.vaadin.hilla.exception.EndpointException;

public class ComponentsNotFoundException extends EndpointException {
    public ComponentsNotFoundException(String message) {
        super(message);
    }
}
