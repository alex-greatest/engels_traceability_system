package com.rena.application.service.traceability.station.order;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeSaveRequest;
import com.rena.application.entity.dto.traceability.station.order.canban.Canban;
import com.rena.application.entity.model.settings.type.BoilerTypeCycle;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.settings.type.BoilerTypeCycleRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.service.settings.shift.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoilerOrderHelperService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final PartLastRepository partLastRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ShiftService shiftService;
    private final BoilerTypeCycleRepository boilerTypeCycleRepository;

    private BoilerOrder createOrder(Canban canban, UserHistory user, Integer shiftNumber, BoilerTypeCycle boilerType) {
        var boilerOrderNew = new BoilerOrder();
        boilerOrderNew.setBoilerTypeCycle(boilerType);
        boilerOrderNew.setId(canban.getId());
        boilerOrderNew.setOrderNumber(canban.getNumberOrder());
        boilerOrderNew.setAmountBoilerOrder(canban.getAmountBoilerOrder());
        boilerOrderNew.setAmountBoilerPrint(0);
        boilerOrderNew.setAmountBoilerMade(canban.getAmountBoilerOrder());
        boilerOrderNew.setUserHistory(user);
        boilerOrderNew.setModifiedDate(LocalDateTime.now());
        boilerOrderNew.setNumberShiftCreated(shiftNumber);
        boilerOrderNew.setStatus(3);
        boilerOrderNew.setCanbanCode(canban.getCanbanCode());
        boilerOrderNew.setMainCode(canban.getMainCode());
        return boilerOrderRepository.save(boilerOrderNew);
    }

    public BoilerOrder getOrder(Canban canban) {
        return boilerOrderRepository.findById(canban.getId()).orElseGet(() -> {
            var boilerType = boilerTypeCycleRepository.findByArticleAndIsActive(canban.getArticle(), true)
                    .orElseThrow(() -> new RecordNotFoundException("Тип котла не найден"));
            var user = userHistoryRepository.
                    findUserHistoryForActiveOperatorByStationName(canban.getStationName()).
                    orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
            var numberShift = shiftService.getCurrentShiftStation().getNumber();
            return createOrder(canban, user, numberShift, boilerType);
        });
    }

    public void checkOrder(BoilerOrder boilerOrder, String nameStation) {
        boolean isOrderReady = boilerOrder.getAmountBoilerPrint() > 0 &&
                boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder();
        if (isOrderReady) {
            partLastRepository.updatePart_idByStation(null, nameStation);
            throw new RecordNotFoundException("Все этикетки заказа распечатаны");
        }
    }

    public BoilerOrder updateOrder(BarcodeSaveRequest barcodeSaveRequest) {
        var boilerOrder = boilerOrderRepository.findById(barcodeSaveRequest.getBoilerOrderId())
                .orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
        if (boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder()) {
            throw new RecordNotFoundException("Все этикетки заказа распечатаны");
        }
        boilerOrder.setAmountBoilerPrint(boilerOrder.getAmountBoilerPrint() + 1);
        return boilerOrderRepository.save(boilerOrder);
    }
}
