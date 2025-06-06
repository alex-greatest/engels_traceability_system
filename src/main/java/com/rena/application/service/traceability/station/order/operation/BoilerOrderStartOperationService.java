package com.rena.application.service.traceability.station.order.operation;

import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeBoilerGetRequest;
import com.rena.application.entity.dto.traceability.station.order.barcode.BarcodeGenerated;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.service.traceability.helper.BoilerOrderHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class BoilerOrderStartOperationService {
    private final SettingRepository settingRepository;

    public BarcodeGenerated generateBarcodeData(@Valid BarcodeBoilerGetRequest barcodeBoilerOrderPrintRequest) {
        var settings = settingRepository.findById(1L).orElseThrow(() ->
                new RecoverableDataAccessException("Настройки не найдены"));
        AtomicReference<Integer> nextBoilerNumber = new AtomicReference<>(settings.getNextBoilerNumber());
        var barcodes = IntStream.range(0, 1)
                .mapToObj(i -> {
                    var serialNumber = BoilerOrderHelper.getSerialNumber(
                            nextBoilerNumber.get(),
                            barcodeBoilerOrderPrintRequest.getArticle(),
                            barcodeBoilerOrderPrintRequest.getTypeLabel());
                    nextBoilerNumber.getAndSet(nextBoilerNumber.get() + 1);
                    return serialNumber;
                }).
                toList();
        return new BarcodeGenerated(barcodes);
    }
}
