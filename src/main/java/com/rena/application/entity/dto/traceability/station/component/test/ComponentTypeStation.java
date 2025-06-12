package com.rena.application.entity.dto.traceability.station.component.test;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ComponentTypeStation(@NotBlank String name, List<String> code, Integer status) {
}
