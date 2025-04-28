package com.rena.application.config.mapper.component.material;

import com.rena.application.entity.dto.traceability.station.components.scanned.material.MaterialOperation;
import com.rena.application.entity.model.settings.component.material.Material;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MaterialMapper {
    Material toEntity(MaterialOperation materialOperation);

    MaterialOperation toMaterialOperation(Material material);

    List<MaterialOperation> toMaterialOperation(List<Material> material);
}