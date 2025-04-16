package com.rena.application.service.traceability.station.wp.one.traceability;

import com.rena.application.entity.dto.traceability.station.wp.one.traceability.SerialLogManualRequest;
import com.rena.application.entity.model.traceability.station.SerialNumberLogManual;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.BoilerRepository;
import com.rena.application.repository.traceability.common.SerialNumberLogManualRepository;
import com.rena.application.repository.user.UserHistoryRepository;
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

    public String getBarcodeLog(String serialNumber) {
        var boiler = boilerRepository.findBySerialNumber(serialNumber).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        return "";
    }

    public void addSerialNumberLogManual(@Valid SerialLogManualRequest serialLogManualRequest) {
        var boiler = boilerRepository.findBySerialNumber(serialLogManualRequest.serialNumber()).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        var userHistory = userHistoryRepository.
                findByCodeAndIsActive(serialLogManualRequest.userCode(), true)
                .orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
        var serialNumberLogManual = new SerialNumberLogManual();
        serialNumberLogManual.setBoiler(boiler);
        serialNumberLogManual.setShiftNumber(serialLogManualRequest.shift());
        serialNumberLogManual.setUserHistory(userHistory);
        serialNumberLogManual.setAmount(serialLogManualRequest.amountPrint());
        serialNumberLogManual.setDateCreate(LocalDateTime.now());
        serialNumberLogManualRepository.save(serialNumberLogManual);
    }
}
