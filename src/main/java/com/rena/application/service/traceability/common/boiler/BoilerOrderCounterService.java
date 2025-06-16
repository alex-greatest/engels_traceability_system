package com.rena.application.service.traceability.common.boiler;

import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerMadeOrderRepository;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoilerOrderCounterService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerMadeOrderRepository boilerMadeOrderRepository;
    private final MainInformationService mainInformationService;

    public void updateOrderCounter(BoilerOrder boilerOrder, StationHistory stationHistory) {
        if (stationHistory.getStationType().getName().equals("Материалы")) {
            updateCounter(boilerOrder);
        } else {
            updateBoilerMadeOrder(boilerOrder, stationHistory.getName());
        }
    }

    private void updateCounter(BoilerOrder boilerOrder) {
        if (boilerOrder.getAmountBoilerMade() + 1 > boilerOrder.getAmountBoilerOrder()) {
            throw new RecordNotFoundException("Все котлы в закказе уже собраны");
        }
        boilerOrder.setAmountBoilerMade(boilerOrder.getAmountBoilerMade() + 1);
        boilerOrderRepository.save(boilerOrder);
    }

    public void updateBoilerMadeOrder(BoilerOrder boilerOrder, String stationName) {
        var boilerMadeOrder = boilerMadeOrderRepository.
                findByStation_NameAndBoilerOrder_Id(stationName, boilerOrder.getId()).
                orElseThrow(() -> new RecordNotFoundException("Не найден счётчик на станции: " + stationName));
        if (boilerMadeOrder.getAmountBoilerMadeOrder() + 1 > boilerOrder.getAmountBoilerOrder()) {
            throw new RecordNotFoundException("Все котлы в закказе на этой станции уже собраны");
        }
        boilerMadeOrder.setAmountBoilerMadeOrder(boilerMadeOrder.getAmountBoilerMadeOrder() + 1);
        boilerMadeOrderRepository.save(boilerMadeOrder);
    }

    public void decreaseCounter(BoilerOrder boilerOrder, StationHistory station) {
        if (station.getStationType().getName().equals("Материалы")) {
            decreaseOrderCounter(boilerOrder);
        } else {
            decreaseBoilerMadeOrder(boilerOrder, station.getName());
        }
    }

    private void decreaseBoilerMadeOrder(BoilerOrder boilerOrder, String stationNameSource) {
        var boilerMadeOrder = boilerMadeOrderRepository.
                findByStation_NameAndBoilerOrder_Id(stationNameSource, boilerOrder.getId()).
                orElseThrow(() -> new RecordNotFoundException("Не найден счётчик на станции: " + stationNameSource));
        if  (boilerMadeOrder.getAmountBoilerMadeOrder() <= 0) {
            return;
        }
        boilerMadeOrder.setAmountBoilerMadeOrder(boilerMadeOrder.getAmountBoilerMadeOrder() - 1);
        boilerMadeOrderRepository.save(boilerMadeOrder);
    }

    private void decreaseOrderCounter(BoilerOrder boilerOrder) {
        if (boilerOrder.getAmountBoilerMade() <= 0) {
            return;
        }
        boilerOrder.setAmountBoilerMade(boilerOrder.getAmountBoilerMade() - 1);
    }
}
