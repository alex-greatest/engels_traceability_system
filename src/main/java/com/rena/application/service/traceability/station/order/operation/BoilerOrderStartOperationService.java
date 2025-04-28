package com.rena.application.service.traceability.station.order.operation;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerOrderPrintRequest;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.service.traceability.helper.WpOneHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class BoilerOrderStartOperationService {
    private final SettingRepository settingRepository;

    public List<String> generateBarcodeData(@Valid BarcodeBoilerOrderPrintRequest barcodeBoilerOrderPrintRequest) {
        var settings = settingRepository.findById(1L).orElseThrow(() ->
                new RecoverableDataAccessException("Настройки не найдены"));
        var amountPrint = barcodeBoilerOrderPrintRequest.amountPrint();
        AtomicReference<Integer> nextBoilerNumber = new AtomicReference<>(settings.getNextBoilerNumber());
        return IntStream.range(0, amountPrint)
                .mapToObj(i -> {
                    var serialNumber = WpOneHelper.getSerialNumber(nextBoilerNumber.get(), barcodeBoilerOrderPrintRequest.article());
                    nextBoilerNumber.getAndSet(nextBoilerNumber.get() + 1);
                    return serialNumber;
                }).
                toList();
    }
}
