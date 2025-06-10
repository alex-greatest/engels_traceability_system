package com.rena.application.entity.dto.traceability.station.components.scanned.component;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ComponentTypeStation(@NotBlank String name, List<String> code, Integer status) {
}
