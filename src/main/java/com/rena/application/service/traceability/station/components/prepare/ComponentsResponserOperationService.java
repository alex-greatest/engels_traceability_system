package com.rena.application.service.traceability.station.components.prepare;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.common.exchange.MainInformation;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentSetOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.material.MaterialOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.traceability.component.ComponentsNotFoundException;
import com.rena.application.service.traceability.station.initialize.MainInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ComponentsResponserOperationService {
    private final MainInformationService mainInformationService;
    private final ComponentsScannedOperationService componentsScannedOperationService;
    private final MaterialScannedOperationService materialScannedOperationService;

    public MainInformation createResponseOperationComponents(String stationName) {
        return mainInformationService.getBoilerMadeInfo(stationName);
    }

    public ComponentsOperationStartResponse createResponseOperationComponents(Boiler boiler, String stationName) {
        var boilerOrder = boiler.getBoilerOrder();
        var componentsScannedOperation = componentsScannedOperationService.getComponentsScanned(boiler, stationName);
        var materialsScannedOperation = materialScannedOperationService.getMaterialsScanned(stationName);
        checkComponents(componentsScannedOperation.components(), materialsScannedOperation.materials());
        var mainInformation = mainInformationService.getBoilerMadeInfo(boilerOrder, stationName);
        var boilerTypeOperation = new BoilerTypeOperation(
                boiler.getBoilerTypeCycle().getTypeName(),
                boiler.getBoilerTypeCycle().getArticle(),
                stationName);
        return new ComponentsOperationStartResponse(
                mainInformation,
                boilerTypeOperation,
                componentsScannedOperation,
                materialsScannedOperation
        );
    }

    private void checkComponents(List<ComponentSetOperation> components, List<MaterialOperation> materials) {
        if (components.isEmpty() && materials.isEmpty()) {
            throw new ComponentsNotFoundException("Компоненты и материалы не найдены. Работа не возможна");
        }
    }
}
