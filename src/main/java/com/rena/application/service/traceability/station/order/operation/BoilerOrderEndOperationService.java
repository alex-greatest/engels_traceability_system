package com.rena.application.service.traceability.station.order.operation;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerOrderSavePrintRequest;
import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerOrderSavePrintResponse;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.traceability.common.station.StationRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.service.settings.shift.ShiftService;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import com.rena.application.service.traceability.station.order.BoilerOrderManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class BoilerOrderEndOperationService {
    private final SettingRepository settingRepository;
    private final StationRepository stationRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final ShiftService shiftService;
    private final PartLastRepository partLastRepository;
    private final BoilerTraceabilityService boilerTraceabilityService;
    private final BoilerOrderManageService boilerOrderManageService;

    @Transactional
    public BarcodeBoilerOrderSavePrintResponse saveBarcodes(@Valid BarcodeBoilerOrderSavePrintRequest wp) {
        UserHistory user = null;
        try {
            user = userHistoryRepository.findByCodeAndIsActive(wp.userCode(), true).
                    orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
            var boilerOrder = boilerOrderManageService.getBoilerOrder(wp.id());
            if (boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder()) {
                partLastRepository.findByStation_Name(wp.stationName()).ifPresent(partLastRepository::delete);
                throw new RecordNotFoundException("Все этикетки уже распечатаны");
            }
            return saveBarcodes(wp, user, boilerOrder);
        } catch (BoilerTypeNotFoundException | BoilerOrderNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private BarcodeBoilerOrderSavePrintResponse saveBarcodes(BarcodeBoilerOrderSavePrintRequest wp, UserHistory user, BoilerOrder boilerOrder) {
        var amountBarcodes = wp.serialNumbers().size();
        var setting = settingRepository.getReferenceById(1L);
        var station = stationRepository.getReferenceById(1L);
        BoilerOrder finalBoilerOrder = boilerOrder;
        wp.serialNumbers().forEach(serialNumber -> {
            var boiler = boilerTraceabilityService.createBoiler(finalBoilerOrder, user, 1, serialNumber, station);
            operationTraceabilityService.createOperation(boiler, wp.numberShift(), wp.stationName(), 0, 1);
        });
        setting.setNextBoilerNumber(setting.getNextBoilerNumber() + amountBarcodes);
        var shiftAmount = shiftService.updateShiftStation(station.getName(), amountBarcodes);
        boilerOrder = boilerOrderManageService.setAmountBoilerPrint(boilerOrder, amountBarcodes);
        return new BarcodeBoilerOrderSavePrintResponse(boilerOrder.getAmountBoilerPrint(), shiftAmount);
    }
}
