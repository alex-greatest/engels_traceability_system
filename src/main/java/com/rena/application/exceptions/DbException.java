package com.rena.application.exceptions;

import com.vaadin.hilla.exception.EndpointException;

public class DbException extends EndpointException {
    public DbException(String message) {
        super(message);
    }
}
