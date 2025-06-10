package com.rena.application.service.traceability.station.components.prepare;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
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
public class ComponentsPrepareOperationService {
    private final MainInformationService mainInformationService;
    private final OperationTraceabilityService operationTraceabilityService;
    private final ComponentsScannedOperationService componentsScannedOperationService;

    @Transactional
    public BoilerMadeInformation createResponseOperationComponents(String stationName) {
        return mainInformationService.getBoilerMadeInfo(stationName);
    }

    @Transactional
    public ComponentsOperationStartResponse createResponseOperationComponents(Boiler boiler, StationHistory stationHistory) {
        var stationName = stationHistory.getName();
        var boilerOrder = boiler.getBoilerOrder();
        var componentsScannedOperation = componentsScannedOperationService.getComponentsScanned(boiler, stationName);
        checkComponents(componentsScannedOperation.getComponents());
        var boilerMadeInformation = mainInformationService.getBoilerMadeInfo(boilerOrder, stationName);
        var boilerTypeOperation = new BoilerTypeOperation(
                boiler.getBoilerTypeCycle().getTypeName(),
                boiler.getBoilerTypeCycle().getArticle(),
                boiler.getSerialNumber());
        operationTraceabilityService.createOperation(boiler, stationHistory, 3, true);
        return new ComponentsOperationStartResponse(
                boilerMadeInformation,
                boilerTypeOperation,
                componentsScannedOperation
        );
    }

    private<T> void checkComponents(List<T> componentsType) {
        if (componentsType.isEmpty()) {
            throw new RecordNotFoundException("На найдены привязанные компоненты. Работа не возможна");
        }
    }
}
