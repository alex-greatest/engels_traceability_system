package com.rena.application.service.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoilerTraceabilityService {
    private final BoilerRepository boilerRepository;

    public Boiler updateBoiler(String serialNumber, Integer status, StationHistory station) {
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(status);
        boiler.setLastStation(station);
        return boiler;
    }
}
