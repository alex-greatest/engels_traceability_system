package com.rena.application.exceptions.result.boiler;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class BoilerPrevStationEmpty extends EndpointException {
    public BoilerPrevStationEmpty(String message) {
        super(message);
    }
}
