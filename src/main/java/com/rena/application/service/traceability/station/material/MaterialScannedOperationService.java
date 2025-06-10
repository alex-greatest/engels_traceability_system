package com.rena.application.service.traceability.station.material;

import com.rena.application.config.mapper.component.material.MaterialMapper;
import com.rena.application.entity.dto.traceability.station.components.scanned.MaterialScannedOperation;
import com.rena.application.entity.model.settings.material.Material;
import com.rena.application.repository.settings.material.MaterialRepository;
import com.rena.application.repository.settings.material.MaterialTypeRepository;
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
        List<Material> materials = materialRepository.findAllMaterialsByStationOrdered(stationName);
        List<String> materialsTypeOperation = materialTypeRepository.findDistinctMaterialTypeNamesByStationName(stationName);
        var materialsOperation = materialMapper.toMaterialOperation(materials);
        return new MaterialScannedOperation(materialsOperation, materialsTypeOperation);
    }
}
