package com.rena.application.service.traceability.station.initialize;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.exchange.MainInformation;
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

    public MainInformation getBoilerMadeInfo(String nameStation) {
        var amountBoilerMadeShift = shiftService.getAmountBoilerMade(nameStation);
        var shiftNumber = shiftService.getCurrentShift().getNumber();
        var boilerMadeInformation = new BoilerMadeInformation(0, 0, amountBoilerMadeShift, shiftNumber);
        var operator = userTraceabilityService.getLastOperatorLogin(nameStation);
        var admin = userTraceabilityService.getLastAdminLogin(nameStation);
        return new MainInformation(boilerMadeInformation, operator, admin);
    }

    public MainInformation getBoilerMadeInfo(BoilerOrder boilerOrder, String nameStation) {
        var boilerOrderInfo = boilerMadeOrderRepository.findByStation_Name(nameStation).
                orElseGet(() -> createBoilerMadeInformation(boilerOrder, nameStation));
        var amountBoilerMadeShift = shiftService.getAmountBoilerMade(nameStation);
        var amountBoilerOrder = boilerOrderInfo.getAmountBoilerMadeOrder();
        var orderNumber = boilerOrder.getOrderNumber();
        var shiftNumber = shiftService.getCurrentShift().getNumber();
        var boilerMadeInformation = new BoilerMadeInformation(orderNumber, amountBoilerOrder, amountBoilerMadeShift, shiftNumber);
        var operator = userTraceabilityService.getLastOperatorLogin(nameStation);
        var admin = userTraceabilityService.getLastAdminLogin(nameStation);
        return new MainInformation(boilerMadeInformation, operator, admin);
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
