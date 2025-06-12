package com.rena.application.service.traceability.station.material;

import com.rena.application.entity.dto.traceability.station.material.result.MaterialResultRequest;
import com.rena.application.entity.dto.traceability.station.material.result.MaterialResultResponse;
import com.rena.application.entity.model.settings.material.MaterialType;
import com.rena.application.entity.model.settings.material.MaterialValue;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.component.ComponentNotFound;
import com.rena.application.repository.settings.material.MaterialTypeRepository;
import com.rena.application.repository.settings.material.MaterialValueRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class MaterialResultSaveService {
    private final BoilerRepository boilerRepository;
    private final MaterialValueRepository materialValueRepository;
    private final MaterialTypeRepository materialTypeRepository;

    @Transactional
    public MaterialResultResponse saveResultsMaterial(@Valid MaterialResultRequest componentResultRequest) {
        try {
            return saveMaterialValue(componentResultRequest);
        } catch (ComponentNotFound e) {
            log.error("Тип материала не найден: {}", componentResultRequest.getName(), e);
            return new MaterialResultResponse(null, null, 2, e.getMessage());
        }
    }

    private MaterialResultResponse saveMaterialValue(MaterialResultRequest materialResultRequest) {
        var boiler = boilerRepository.findBySerialNumber(materialResultRequest.getSerialNumber())
                .orElseThrow(() -> new RecordNotFoundException("Не найден котел с серийным номером: " +
                        materialResultRequest.getSerialNumber()));
        var materialTypeOptional = materialTypeRepository.
                findByCodeAndName(materialResultRequest.getCode(), materialResultRequest.getName());
        var materialType = checkMaterialTypeExists(materialTypeOptional);
        return createSuccessMaterial(materialResultRequest.getValue(), boiler, materialType);
    }

    private MaterialType checkMaterialTypeExists(Optional<MaterialType> materialType) {
        if (materialType.isEmpty()) {
            throw new ComponentNotFound("Тип матеариала не найден");
        }
        return materialType.get();
    }

    public MaterialResultResponse createSuccessMaterial(String value, Boiler boiler, MaterialType materialType) {
        var materialValue = new MaterialValue();
        materialValue.setValue(value);
        materialValue.setMaterialType(materialType);
        materialValue.setBoiler(boiler);
        materialValueRepository.save(materialValue);
        return new MaterialResultResponse(materialType.getName(), materialValue.getValue(), 1, null);
    }
}
