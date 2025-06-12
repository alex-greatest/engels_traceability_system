package com.rena.application.entity.dto.traceability.station.material.test;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record MaterialTypeStation(@NotBlank String name, List<String> code, Integer status) {
}
