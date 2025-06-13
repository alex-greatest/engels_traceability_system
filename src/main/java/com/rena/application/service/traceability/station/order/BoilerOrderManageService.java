package com.rena.application.service.traceability.station.order;

import com.rena.application.entity.dto.traceability.station.order.BoilerOrderOperationResponse;
import com.rena.application.entity.dto.traceability.station.order.canban.Canban;
import com.rena.application.entity.model.settings.PartLast;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerOrderHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.service.traceability.station.order.history.BoilerOrderHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
@Slf4j
public class BoilerOrderManageService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerOrderHelperService boilerOrderHelperService;
    private final PartLastRepository partLastRepository;
    private final StationRepository stationRepository;
    private final BoilerOrderHistoryService boilerOrderHistoryService;
    private final BoilerRepository boilerRepository;

    public BoilerOrder getBoilerOrder(String id) {
        return boilerOrderRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
    }

    @Transactional
    public BoilerOrderOperationResponse getOrder(@Valid Canban canban) {
        try {
            var boilerOrder = boilerOrderHelperService.getOrder(canban);
            boilerOrderHelperService.checkOrder(boilerOrder, canban.getStationName());
            updatePartLast(canban.getStationName(), boilerOrder.getId());
            return new BoilerOrderOperationResponse(boilerOrder.getId(), boilerOrder.getOrderNumber(),
                    boilerOrder.getBoilerTypeCycle().getArticle(),
                    boilerOrder.getAmountBoilerOrder(), boilerOrder.getAmountBoilerPrint(),
                    boilerOrder.getMainCode(), boilerOrder.getCanbanCode());
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private void updatePartLast(String stationName, String partId) {
        var station = stationRepository.findByName(stationName)
                .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var partLast = partLastRepository.findByStation_Name(stationName).orElseGet(()-> {
            var newPartLast = new PartLast();
            newPartLast.setStation(station);
            return partLastRepository.save(newPartLast);
        });
        partLast.setPart_id(partId);
        partLastRepository.save(partLast);
    }

    public void checkBoilerOrder(Boiler boiler, String boilerOrderId) {
        var boilerOrder = boilerOrderRepository.findById(boilerOrderId)
                .orElseThrow(() -> new RecordNotFoundException("Не найден заказ котла с ID: " +
                        boilerOrderId));
        if (boiler.getBoilerOrder().getId().equals(boilerOrder.getId())) {
            return;
        }
        updateBoilerOrderAssignedBoiler(boiler, boilerOrder);
        boilerOrderHistoryService.createBoilerOrderHistory(boilerOrder, boiler);

    }

    private void updateBoilerOrderAssignedBoiler(Boiler boiler, BoilerOrder boilerOrder) {
        if (!boiler.getBoilerTypeCycle().getArticle().equals(boilerOrder.getBoilerTypeCycle().getArticle())) {
            throw new RecordNotFoundException("Тип котла не соотвествует заказу: " + boilerOrder.getId());
        }
        boiler.setBoilerOrder(boilerOrder);
        boilerRepository.save(boiler);
    }

    public void interruptedOperation(String stationName) {
        updatePartLast(stationName, null);
    }
}
