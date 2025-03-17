package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class BoilerTypeNotFoundException extends EndpointException {
    public BoilerTypeNotFoundException(String message) {
        super(message);
    }
}
