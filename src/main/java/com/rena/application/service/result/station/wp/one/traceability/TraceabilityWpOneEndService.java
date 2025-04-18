package com.rena.application.service.result.station.wp.one.traceability;

import com.rena.application.entity.dto.result.station.wp.one.BarcodeSaveOneWpRequest;
import com.rena.application.entity.dto.result.station.wp.one.BarcodeSaveOneWpResponse;
import com.rena.application.entity.model.result.common.Boiler;
import com.rena.application.entity.model.result.common.Operation;
import com.rena.application.entity.model.result.common.Station;
import com.rena.application.entity.model.result.common.Status;
import com.rena.application.entity.model.result.station.wp.one.order.BoilerOrder;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerOrderNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.result.common.BoilerRepository;
import com.rena.application.repository.result.common.OperationRepository;
import com.rena.application.repository.result.common.StationRepository;
import com.rena.application.repository.result.common.StatusRepository;
import com.rena.application.repository.result.station.wp.BoilerOrderRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import com.rena.application.service.result.ErrorService;
import com.rena.application.service.shift.ShiftService;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class TraceabilityWpOneEndService {
    private final SettingRepository settingRepository;
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerRepository boilerRepository;
    private final StatusRepository statusRepository;
    private final StationRepository stationRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ErrorService errorService;
    private final OperationRepository operationRepository;
    private final ShiftService shiftService;
    private final PartLastRepository partLastRepository;

    @Transactional
    public BarcodeSaveOneWpResponse saveBarcodes(@Valid BarcodeSaveOneWpRequest wp) {
        UserHistory user = null;
        try {
            user = userHistoryRepository.findByCodeAndIsActive(wp.userCode(), true).
                    orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
            var boilerOrder = boilerOrderRepository.findById(wp.id()).
                    orElseThrow(() -> new BoilerOrderNotFoundException("Заказ не найден"));
            if (boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder()) {
                partLastRepository.findByStation_Name("wp1").ifPresent(partLastRepository::delete);
                throw new RecordNotFoundException("Все этикетки уже распечатаны");
            }
            return saveBarcodes(wp, user, boilerOrder);
        } catch (BoilerTypeNotFoundException | BoilerOrderNotFoundException e) {
            log.error(e.getMessage(), e);
            errorSave(user, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private BarcodeSaveOneWpResponse saveBarcodes(BarcodeSaveOneWpRequest wp, UserHistory user, BoilerOrder boilerOrder) {
        var amountBarcodes = wp.serialNumbers().size();
        var status = statusRepository.getReferenceById(1L);
        var setting = settingRepository.getReferenceById(1L);
        var station = stationRepository.getReferenceById(1L);
        wp.serialNumbers().forEach(serialNumber -> {
            var boiler = createBoiler(boilerOrder, user, status, serialNumber, station, LocalDateTime.now());
            boilerRepository.save(boiler);
            createOperation(boiler, user, status, station, LocalDateTime.now(), wp.numberShift());
        });
        setting.setNextBoilerNumber(setting.getNextBoilerNumber() + amountBarcodes);
        var shiftAmount = shiftService.updateShiftStation(station.getName(), amountBarcodes);
        boilerOrder.setAmountBoilerPrint(boilerOrder.getAmountBoilerPrint() + amountBarcodes);
        return new BarcodeSaveOneWpResponse(boilerOrder.getAmountBoilerPrint(), shiftAmount);
    }

    private Boiler createBoiler(BoilerOrder boilerOrder, UserHistory userHistory, Status status,
                                String serialNumber, Station station, LocalDateTime now) {
        var boiler = new Boiler();
        boiler.setBoilerOrder(boilerOrder);
        boiler.setUserHistory(userHistory);
        boiler.setStatus(status);
        boiler.setSerialNumber(serialNumber);
        boiler.setBoilerTypeCycle(boilerOrder.getBoilerTypeCycle());
        boiler.setDateCreate(now);
        boiler.setLastStation(station);
        return boilerRepository.save(boiler);
    }

    public void createOperation(Boiler boiler, UserHistory userHistory, Status status, Station station,
                                LocalDateTime now, Integer numberShift) {
        var operation = new Operation();
        operation.setBoiler(boiler);
        operation.setUserHistory(userHistory);
        operation.setDateCreate(now);
        operation.setStatus(status);
        operation.setStation(station);
        operation.setNumberShift(numberShift);
        operation.setIsLast(true);
        operationRepository.save(operation);
    }

    private void errorSave(UserHistory userHistory, EndpointException e) {
        if (userHistory != null) {
            errorService.addError(e.getMessage(), "wp1", "Печать этикеток", userHistory, null);
        }
    }
}
