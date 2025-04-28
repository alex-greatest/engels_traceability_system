package com.rena.application.service.traceability.station.components.responser;

import com.rena.application.config.mapper.component.material.MaterialMapper;
import com.rena.application.entity.dto.traceability.station.components.scanned.MaterialScannedOperation;
import com.rena.application.entity.model.settings.component.material.Material;
import com.rena.application.entity.model.settings.component.material.MaterialType;
import com.rena.application.repository.settings.component.material.MaterialRepository;
import com.rena.application.repository.settings.component.material.MaterialTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialScannedOperationService {
    private final MaterialTypeRepository materialTypeRepository;
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    public MaterialScannedOperation getMaterialsScanned(String stationName) {
        var materialsType = materialTypeRepository.findMaterialTypesByStationName(stationName);
        var materials = getMaterial(materialsType, stationName);
        var materialsOperation = materialMapper.toMaterialOperation(materials);
        var materialsTypeOperation = materialsType.stream().map(MaterialType::getName).toList();
        return new MaterialScannedOperation(materialsOperation, materialsTypeOperation);
    }

    private List<Material> getMaterial(List<MaterialType> materialsType,
                                       String stationName) {
        var materialTypesId = materialsType.stream().map(MaterialType::getId).toList();
        return materialRepository.findAllMaterialsByTypeIdsAndStationOrdered(materialTypesId, stationName);
    }
}
