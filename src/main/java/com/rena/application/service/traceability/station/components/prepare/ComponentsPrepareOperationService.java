package com.rena.application.service.traceability.station.components.prepare;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsMaterials;
import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentSetOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.material.MaterialOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ComponentsPrepareOperationService {
    private final MainInformationService mainInformationService;
    private final ComponentsScannedOperationService componentsScannedOperationService;
    private final MaterialScannedOperationService materialScannedOperationService;

    @Transactional
    public BoilerMadeInformation createResponseOperationComponents(String stationName) {
        return mainInformationService.getBoilerMadeInfo(stationName);
    }

    @Transactional
    public ComponentsOperationStartResponse createResponseOperationComponents(Boiler boiler, String stationName) {
        var boilerOrder = boiler.getBoilerOrder();
        var componentsScannedOperation = componentsScannedOperationService.getComponentsScanned(boiler, stationName);
        var materialsScannedOperation = materialScannedOperationService.getMaterialsScanned(stationName);
        checkComponents(componentsScannedOperation.components(), materialsScannedOperation.materials());
        var componentsMaterials = createMaterialComponents(componentsScannedOperation.components(), materialsScannedOperation.materials());
        var boilerMadeInformation = mainInformationService.getBoilerMadeInfo(boilerOrder, stationName);
        var boilerTypeOperation = new BoilerTypeOperation(
                boiler.getBoilerTypeCycle().getTypeName(),
                boiler.getBoilerTypeCycle().getArticle(),
                boiler.getSerialNumber());
        return new ComponentsOperationStartResponse(
                boilerMadeInformation,
                boilerTypeOperation,
                componentsScannedOperation,
                materialsScannedOperation,
                componentsMaterials
        );
    }

    private void checkComponents(List<ComponentSetOperation> components, List<MaterialOperation> materials) {
        if (components.isEmpty() && materials.isEmpty()) {
            throw new RecordNotFoundException("Компоненты и материалы не найдены. Работа не возможна");
        }
    }

    private List<ComponentsMaterials> createMaterialComponents(List<ComponentSetOperation> components,
                                                               List<MaterialOperation> materials) {
        var componentsMaterials = new ArrayList<ComponentsMaterials>();
        var componentsMaterialFromComponents = components.stream().
                map(c -> new ComponentsMaterials(c.componentTypeName(), c.value())).
                toList();
        var componentsMaterialFromMaterials = materials.stream().
                map(m -> new ComponentsMaterials(m.name(), m.value())).
                toList();
        componentsMaterials.addAll(componentsMaterialFromComponents);
        componentsMaterials.addAll(componentsMaterialFromMaterials);
        return componentsMaterials;
    }
}
