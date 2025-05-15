package com.rena.application.service.traceability.common.initialize;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.initialize.ShiftNumber;
import com.rena.application.entity.model.traceability.common.boiler.BoilerMadeCountOrder;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerMadeOrderRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.service.settings.shift.ShiftService;
import com.rena.application.service.traceability.common.user.UserTraceabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainInformationService {
    private final BoilerMadeOrderRepository boilerMadeOrderRepository;
    private final ShiftService shiftService;
    private final StationRepository stationRepository;
    private final UserTraceabilityService userTraceabilityService;

    public BoilerMadeInformation getBoilerMadeInfo(String nameStation) {
        return new BoilerMadeInformation(0, 0, 0);
    }

    public BoilerMadeInformation getBoilerMadeInfo(BoilerOrder boilerOrder, String nameStation) {
        var boilerOrderInfo = boilerMadeOrderRepository.findByStation_Name(nameStation).
                orElseGet(() -> createBoilerMadeInformation(boilerOrder, nameStation));
        var amountBoilerMadeOrder = boilerOrderInfo.getAmountBoilerMadeOrder();
        var amountBoilerOrder = boilerOrder.getAmountBoilerOrder();
        var orderNumber = boilerOrder.getOrderNumber();
        return new BoilerMadeInformation(orderNumber, amountBoilerMadeOrder, amountBoilerOrder);
    }

    public BoilerMadeCountOrder createBoilerMadeInformation(BoilerOrder boilerOrder, String nameStation) {
        var station = stationRepository.findByName(nameStation)
                .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var boilerMadeOrder = new BoilerMadeCountOrder();
        boilerMadeOrder.setBoilerOrder(boilerOrder);
        boilerMadeOrder.setAmountBoilerMadeOrder(0);
        boilerMadeOrder.setStation(station);
        return boilerMadeOrderRepository.save(boilerMadeOrder);
    }
}
