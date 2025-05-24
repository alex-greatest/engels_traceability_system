package com.rena.application.service.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerMadeOrderRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoilerTraceabilityService {
    private final BoilerRepository boilerRepository;
    private final StationHistoryRepository stationHistoryRepository;
    private final BoilerMadeOrderRepository boilerMadeOrderRepository;

    public void boilerSave(Boiler boiler) {
        boilerRepository.save(boiler);
    }

    public Boiler createBoiler(BoilerOrder boilerOrder, UserHistory userHistory, Integer status,
                               String serialNumber, StationHistory stationHistory) {
        var boiler = new Boiler();
        boiler.setBoilerOrder(boilerOrder);
        boiler.setUserHistory(userHistory);
        boiler.setStatus(status);
        boiler.setSerialNumber(serialNumber);
        boiler.setBoilerTypeCycle(boilerOrder.getBoilerTypeCycle());
        boiler.setDateCreate(LocalDateTime.now());
        boiler.setLastStation(stationHistory);
        return boilerRepository.save(boiler);
    }

    public Boiler updateBoiler(String serialNumber, StationHistory station, Integer status, String stationNameSource) {
        return updateBoiler(serialNumber, status, station, stationNameSource);
    }

    public Boiler updateBoiler(String serialNumber, String stationName, Integer status) {
        var station = stationHistoryRepository.findByName(stationName).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        return updateBoiler(serialNumber, status, station, null);
    }

    private Boiler updateBoiler(String serialNumber, Integer status, StationHistory station, String stationNameSource) {
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new RecordNotFoundException("Котел не найден"));
        boiler.setDateUpdate(LocalDateTime.now());
        boiler.setStatus(status);
        boiler.setLastStation(station);
        boilerRepository.save(boiler);
        if (!station.getName().equals("Доработка") && status == 1) {
            updateBoilerMadeOrder(boiler.getBoilerOrder(), station.getName());
        } else if (station.getName().equals("Доработка") && stationNameSource != null) {
            decreaseBoilerMadeOrder(boiler.getBoilerOrder(), stationNameSource);
        }
        return boiler;
    }

    private void updateBoilerMadeOrder(BoilerOrder boilerOrder, String stationName) {
        var boilerMadeOrder = boilerMadeOrderRepository.
                findByStation_NameAndBoilerOrder_Id(stationName, boilerOrder.getId()).
                orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
        boilerMadeOrder.setAmountBoilerMadeOrder(boilerMadeOrder.getAmountBoilerMadeOrder() + 1);
        boilerMadeOrderRepository.save(boilerMadeOrder);
    }

    private void decreaseBoilerMadeOrder(BoilerOrder boilerOrder, String stationNameSource) {
        var boilerMadeOrder = boilerMadeOrderRepository.
                findByStation_NameAndBoilerOrder_Id(stationNameSource, boilerOrder.getId()).
                orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
        if  (boilerMadeOrder.getAmountBoilerMadeOrder() <= 0) {
            return;
        }
        boilerMadeOrder.setAmountBoilerMadeOrder(boilerMadeOrder.getAmountBoilerMadeOrder() - 1);
        boilerMadeOrderRepository.save(boilerMadeOrder);
    }
}
