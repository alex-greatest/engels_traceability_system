package com.rena.application.service.traceability.station.order.log;

import com.rena.application.entity.dto.traceability.station.order.log.SerialLogManualRequest;
import com.rena.application.entity.model.traceability.station.order.SerialNumberLogManual;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.repository.traceability.common.log.SerialNumberLogManualRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.service.settings.shift.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Validated
public class SerialNumberLogManualService {
    private final SerialNumberLogManualRepository serialNumberLogManualRepository;
    private final BoilerRepository boilerRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ShiftService shiftService;

    public String getBarcodeLog(String serialNumber) {
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        return "";
    }

    public void addSerialNumberLogManual(@Valid SerialLogManualRequest serialLogManualRequest) {
        var boiler = boilerRepository.findBySerialNumber(serialLogManualRequest.getSerialNumber()).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        var user = userHistoryRepository.
                findUserHistoryForActiveOperatorByStationName(serialLogManualRequest.getStationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        var numberShift = shiftService.getCurrentShiftStation().getNumber();
        var serialNumberLogManual = new SerialNumberLogManual();
        serialNumberLogManual.setBoiler(boiler);
        serialNumberLogManual.setShiftNumber(numberShift);
        serialNumberLogManual.setUserHistory(user);
        serialNumberLogManual.setAmount(serialLogManualRequest.getAmountPrint());
        serialNumberLogManual.setDateCreate(LocalDateTime.now());
        serialNumberLogManualRepository.save(serialNumberLogManual);
    }
}
