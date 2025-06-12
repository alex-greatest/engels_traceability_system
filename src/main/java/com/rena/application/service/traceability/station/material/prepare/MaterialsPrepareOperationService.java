package com.rena.application.service.traceability.station.material.prepare;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.station.material.operation.MaterialsOperationStartResponse;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MaterialsPrepareOperationService {
    private final MainInformationService mainInformationService;
    private final OperationTraceabilityService operationTraceabilityService;
    private final MaterialsScannedOperationService materialsScannedOperationService;

    @Transactional
    public MaterialsOperationStartResponse createResponseOperationMaterials(Boiler boiler, StationHistory stationHistory) {
        var stationName = stationHistory.getName();
        var boilerOrder = boiler.getBoilerOrder();
        var materialScannedOperation = materialsScannedOperationService.getMaterialsScanned(stationName);
        checkMaterials(materialScannedOperation.getMaterials());
        var boilerMadeInformation = mainInformationService.getBoilerMadeInfo(boilerOrder, stationName);
        var boilerTypeOperation = new BoilerTypeOperation(
                boiler.getBoilerTypeCycle().getTypeName(),
                boiler.getBoilerTypeCycle().getArticle(),
                boiler.getSerialNumber());
        operationTraceabilityService.createOperation(boiler, stationHistory, 3, true);
        return new MaterialsOperationStartResponse(
                boilerMadeInformation,
                boilerTypeOperation,
                materialScannedOperation
        );
    }

    private<T> void checkMaterials(List<T> materialsType) {
        if (materialsType.isEmpty()) {
            throw new RecordNotFoundException("На найдены привязанные материалы. Работа не возможна");
        }
    }
}
