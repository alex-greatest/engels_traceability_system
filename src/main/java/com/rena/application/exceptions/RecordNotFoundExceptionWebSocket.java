package com.rena.application.exceptions;

import com.vaadin.hilla.exception.EndpointException;
import lombok.Getter;

@Getter
public class RecordNotFoundExceptionWebSocket extends EndpointException {
    private final String station;
    public RecordNotFoundExceptionWebSocket(String message, String station) {
        super(message);
        this.station = station;
    }
}
