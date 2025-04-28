package com.rena.application.service.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoilerTraceabilityService {
    private BoilerRepository boilerRepository;

    public void boilerSave(Boiler boiler) {
        boilerRepository.save(boiler);
    }

    public Boiler createBoiler(BoilerOrder boilerOrder, UserHistory userHistory, Integer status,
                                String serialNumber, Station station) {
        var boiler = new Boiler();
        boiler.setBoilerOrder(boilerOrder);
        boiler.setUserHistory(userHistory);
        boiler.setStatus(status);
        boiler.setSerialNumber(serialNumber);
        boiler.setBoilerTypeCycle(boilerOrder.getBoilerTypeCycle());
        boiler.setDateCreate(LocalDateTime.now());
        boiler.setLastStation(station);
        return boilerRepository.save(boiler);
    }
}
