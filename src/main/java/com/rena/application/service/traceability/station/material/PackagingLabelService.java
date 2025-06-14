package com.rena.application.service.traceability.station.material;

import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.station.material.PackagingLabel;
import com.rena.application.entity.model.traceability.station.material.PackagingLabelHistory;
import com.rena.application.repository.traceability.station.materials.PackagingLabelHistoryRepository;
import com.rena.application.repository.traceability.station.materials.PackagingLabelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class PackagingLabelService {
    private final PackagingLabelRepository packagingLabelRepository;
    private final PackagingLabelHistoryRepository packagingLabelHistoryRepository;

    public PackagingLabel savePackagingLabel(Integer amountPrint) {
        var packagingLabel = new PackagingLabel();
        packagingLabel.setAmount(amountPrint);
        packagingLabel.setDateCreate(LocalDateTime.now());
        return packagingLabelRepository.save(packagingLabel);
    }

    public void savePackagingHistoryLabel(Integer amountPrint, Operation operation) {
        var packagingHistoryLabel = new PackagingLabelHistory();
        packagingHistoryLabel.setAmount(amountPrint);
        packagingHistoryLabel.setDateCreate(LocalDateTime.now());
        packagingHistoryLabel.setOperation(operation);
        packagingHistoryLabel.setDateCreate(LocalDateTime.now());
        packagingLabelHistoryRepository.save(packagingHistoryLabel);
    }

}
