package com.rena.application.service.traceability.station.material;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.station.material.MaterialsResultSave;
import com.rena.application.entity.dto.traceability.station.material.operation.MaterialsOperationSaveResultRequest;
import com.rena.application.entity.model.settings.material.MaterialValue;
import com.rena.application.entity.model.settings.user.UserHistory;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.station.material.MaterialResult;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.material.MaterialValueRepository;
import com.rena.application.repository.settings.material.MaterialTypeRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
import com.rena.application.repository.traceability.station.materials.MaterialResultRepository;
import com.rena.application.service.traceability.common.boiler.BoilerTraceabilityService;
import com.rena.application.service.traceability.common.initialize.MainInformationService;
import com.rena.application.service.traceability.common.operation.OperationTraceabilityService;
import com.rena.application.service.traceability.station.order.BoilerOrderManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class MaterialsResultSaveService {
    private final MaterialTypeRepository materialTypeRepository;
    private final MaterialValueRepository materialValueRepository;
    private final MaterialResultRepository materialResultRepository;
    private final OperationTraceabilityService operationTraceabilityService;
    private final PartLastRepository partLastRepository;
    private final BoilerTraceabilityService boilerTraceabilityService;
    private final MainInformationService mainInformationService;
    private final UserHistoryRepository userHistoryRepository;
    private final BoilerOrderManageService boilerOrderManageService;
    private final PackagingLabelService packagingLabelService;

    @Transactional
    public BoilerMadeInformation saveResultsMaterial(@Valid MaterialsOperationSaveResultRequest materialsOperationSaveResultRequest) {
        var admin = isRequiredAdmin(materialsOperationSaveResultRequest.getIsIgnoringError(),
                materialsOperationSaveResultRequest.getAdminIgnoringError());
        var operation = operationTraceabilityService.updateOperation(
                materialsOperationSaveResultRequest.getStationName(),
                3,
                1,
                null,
                materialsOperationSaveResultRequest.getIsIgnoringError(),
                admin);
        boilerOrderManageService.checkBoilerOrder(operation.getBoiler(), materialsOperationSaveResultRequest.getBoilerOrderId());
        var materialsResultSaves = materialsOperationSaveResultRequest.getMaterialsResultSaves();
        saveMaterialsResult(materialsResultSaves, operation);
        saveMaterialValue(materialsOperationSaveResultRequest.getIsIgnoringError(), materialsResultSaves, operation.getBoiler());
        var boiler = boilerTraceabilityService.updateBoiler(materialsOperationSaveResultRequest.getSerialNumber(),
                materialsOperationSaveResultRequest.getStationName(),
                1);
        var packagingLabel = packagingLabelService.savePackagingLabel(materialsOperationSaveResultRequest.getAmountCopy());
        packagingLabelService.savePackagingHistoryLabel(materialsOperationSaveResultRequest.getAmountCopy(), operation);
        boiler.setPackagingLabel(packagingLabel);
        partLastRepository.updatePart_idByStation(null, materialsOperationSaveResultRequest.getStationName());
        return mainInformationService.
                getBoilerMadeInfo(boiler.getBoilerOrder(), materialsOperationSaveResultRequest.getStationName());
    }

    private void saveMaterialsResult(List<MaterialsResultSave> materialsResultSaves, Operation operation) {
        materialsResultSaves.forEach(componentResult -> saveMaterial(componentResult, operation));
    }

    private void saveMaterial(MaterialsResultSave materialsResultSave, Operation operation) {
        var material = new MaterialResult();
        material.setName(materialsResultSave.name());
        material.setValue(materialsResultSave.scannedValue());
        material.setOperation(operation);
        material.setStatus(materialsResultSave.status());
        material.setCode(materialsResultSave.scannedCode());
        materialResultRepository.save(material);
    }

    private void saveMaterialValue(Boolean isIgnoring, List<MaterialsResultSave> materialsResultSaves, Boiler boiler) {
        if (!isIgnoring) {
            return;
        }
        materialsResultSaves.stream().filter(materialsResultSave -> materialsResultSave.status().equals(2))
                .forEach(materialResult -> saveMaterialValueValue(materialResult, boiler));

    }

    private void saveMaterialValueValue(MaterialsResultSave materialResult, Boiler boiler) {
        var materialType = materialTypeRepository.findByCodeAndName(materialResult.scannedCode(), materialResult.name())
                .orElseThrow(() -> new RecordNotFoundException("Тип материала не найден: " + materialResult.name()));
        var value = new MaterialValue();
        value.setBoiler(boiler);
        value.setMaterialType(materialType);
        value.setValue(materialResult.scannedValue());
        materialValueRepository.save(value);
    }

    private UserHistory isRequiredAdmin(Boolean isIgnoring, Integer code) {
        if (!isIgnoring) {
            return null;
        }
        return userHistoryRepository.findByCodeAndIsActive(code, true).
                orElseThrow(() -> new RecordNotFoundException("Администратор не найден"));
    }
}