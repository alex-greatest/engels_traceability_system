package com.rena.application.exceptions;

import com.vaadin.hilla.exception.EndpointException;

public class RecordNotFoundException extends EndpointException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
