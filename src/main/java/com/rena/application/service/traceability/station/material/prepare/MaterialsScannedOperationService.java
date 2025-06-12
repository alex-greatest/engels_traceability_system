package com.rena.application.service.traceability.station.material.prepare;

import com.rena.application.entity.dto.traceability.station.material.test.MaterialScannedOperation;
import com.rena.application.entity.dto.traceability.station.material.test.MaterialTypeStation;
import com.rena.application.entity.model.settings.material.MaterialStation;
import com.rena.application.entity.model.settings.material.MaterialType;
import com.rena.application.repository.settings.material.MaterialStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialsScannedOperationService {
    private final MaterialStationRepository materialStationRepository;

    public MaterialScannedOperation getMaterialsScanned(String stationName) {
        var materialStations = materialStationRepository.findByStation_Name(stationName);
        var materialsType = materialStations.stream()
                .map(MaterialStation::getMaterialType)
                .collect(Collectors.groupingBy(
                        MaterialType::getName,
                        LinkedHashMap::new,
                        Collectors.mapping(MaterialType::getCode, Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> new MaterialTypeStation(entry.getKey(), entry.getValue(), null))
                .toList();
        return new MaterialScannedOperation(materialsType);
    }
}
