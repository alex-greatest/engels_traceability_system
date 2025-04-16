package com.rena.application.exceptions.traceability.boiler;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class BoilerPrevStationEmpty extends EndpointException {
    public BoilerPrevStationEmpty(String message) {
        super(message);
    }
}
