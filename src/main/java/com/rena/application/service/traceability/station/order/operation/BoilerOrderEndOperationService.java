package com.rena.application.service.traceability.station.order.operation;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeSaveRequest;
import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeSaveResponse;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.boiler.BoilerOrderHistory;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.entity.model.traceability.station.order.BoilerLabelCount;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerLabelCountRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerOrderHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import com.rena.application.service.traceability.station.order.BoilerOrderHelperService;
import com.rena.application.service.traceability.station.order.history.BoilerOrderHistoryService;
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
public class BoilerOrderEndOperationService {
    private final SettingRepository settingRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final StationHistoryRepository stationHistoryRepository;
    private final BoilerOrderHelperService boilerOrderHelperService;
    private final BoilerRepository boilerRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final BoilerLabelCountRepository boilerLabelCountRepository;
    private final PartLastRepository partLastRepository;
    private final BoilerOrderHistoryService boilerOrderHistoryService;

    @Transactional
    public BarcodeSaveResponse saveBarcodes(@Valid BarcodeSaveRequest barcodeSaveRequest) {
        try {
            var station = stationHistoryRepository
                    .findByName(barcodeSaveRequest.getStation())
                    .orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
            var user = userHistoryRepository.
                    findUserHistoryForActiveOperatorByStationName(station.getName()).
                    orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
            var boilerOrder = boilerOrderHelperService.updateOrder(barcodeSaveRequest);
            var boiler = createBoiler(barcodeSaveRequest, station, boilerOrder, user);
            boilerOrderHistoryService.createBoilerOrderHistory(boilerOrder, boiler);
            updateBoilerLabelCount(boiler);
            operationTraceabilityService.createOperation(boiler, station, user, 1, false);
            checkBoilerOrder(boilerOrder, station.getName());
            updateSettings();
            return new BarcodeSaveResponse(boilerOrder.getAmountBoilerPrint());
        } catch (BoilerTypeNotFoundException | BoilerOrderNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private Boiler createBoiler(BarcodeSaveRequest barcodeSaveRequest,
                               StationHistory station,
                               BoilerOrder boilerOrder,
                               UserHistory user) {
        var boiler = new Boiler();
        boiler.setSerialNumber(barcodeSaveRequest.getSerialNumber());
        boiler.setLastStation(station);
        boiler.setBoilerOrder(boilerOrder);
        boiler.setBoilerTypeCycle(boilerOrder.getBoilerTypeCycle());
        boiler.setDateCreate(LocalDateTime.now());
        boiler.setStatus(1);
        boiler.setUserHistory(user);
        return boilerRepository.save(boiler);
    }

    public void updateBoilerLabelCount(Boiler boiler) {
        var boilerLabelCount = boilerLabelCountRepository.
                findByBoiler_SerialNumber(boiler.getSerialNumber()).
                orElseGet(() -> {
                    var newBoilerLabelCount = new BoilerLabelCount();
                    newBoilerLabelCount.setBoiler(boiler);
                    newBoilerLabelCount.setAmountPrintType(0);
                    return newBoilerLabelCount;
                });
        boilerLabelCount.setAmountPrintType(boilerLabelCount.getAmountPrintType() + 1);
        boilerLabelCountRepository.save(boilerLabelCount);
    }

    private void updateSettings() {
        var setting = settingRepository.getReferenceById(1L);
        setting.setNextBoilerNumber(setting.getNextBoilerNumber() + 1);
    }

    private void checkBoilerOrder(BoilerOrder boilerOrder, String stationName) {
        if (boilerOrder.getAmountBoilerPrint() < boilerOrder.getAmountBoilerOrder()) {
            return;
        }
        partLastRepository.findByStation_Name(stationName).ifPresent(p -> {
            p.setPart_id(null);
            partLastRepository.save(p);
        });
    }
}
