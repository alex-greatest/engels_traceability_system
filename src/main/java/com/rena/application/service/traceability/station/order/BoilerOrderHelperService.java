package com.rena.application.service.traceability.station.order;

import com.rena.application.entity.dto.traceability.station.order.canban.Canban;
import com.rena.application.entity.dto.traceability.station.order.canban.CanbanUniqueCode;
import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoilerOrderHelperService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final PartLastRepository partLastRepository;

    private BoilerOrder createOrder(String id, UserHistory user, LocalDateTime now, Integer shiftNumber) {
        var boilerOrderNew = new BoilerOrder();
        boilerOrderNew.setId(id);
        boilerOrderNew.setScanCode("");
        boilerOrderNew.setOrderNumber(0);
        boilerOrderNew.setAmountBoilerOrder(0);
        boilerOrderNew.setAmountBoilerPrint(0);
        boilerOrderNew.setAmountBoilerMade(0);
        boilerOrderNew.setUserHistory(user);
        boilerOrderNew.setModifiedDate(now);
        boilerOrderNew.setNumberShiftCreated(shiftNumber);
        return boilerOrderRepository.save(boilerOrderNew);
    }

    public BoilerOrder getOrder(CanbanUniqueCode canbanUniqueCode, LocalDateTime now, UserHistory user) {
        var boilerOrderOptional = boilerOrderRepository.findById(canbanUniqueCode.id());
        return boilerOrderOptional.orElseGet(() -> createOrder(canbanUniqueCode.id(), user, now, canbanUniqueCode.numberShift()));
    }

    public void checkOrder(BoilerOrder boilerOrder) {
        boolean isOrderReady = boilerOrder.getAmountBoilerPrint() != 0 &&
                boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder();
        if (isOrderReady) {
            partLastRepository.updatePart_idByStation(null, "wp1");
            throw new BoilerOrderReadyNotFoundException("Все этикетки заказа распечатаны");
        }
    }

    public void updateOrder(Canban canban, BoilerOrder boilerOrder, BoilerTypeCycle boilerTypeCycle) {
        boilerOrder.setStatus(3);
        boilerOrder.setBoilerTypeCycle(boilerTypeCycle);
        boilerOrder.setScanCode(canban.code());
        boilerOrder.setOrderNumber(canban.numberOrder());
        boilerOrder.setAmountBoilerOrder(canban.amountBoilerOrder());
    }
}
