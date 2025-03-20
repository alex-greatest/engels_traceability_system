package com.rena.application.entity.dto.result.print;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ComponentsResults(
    @NotNull @Nonnull List<@Nonnull ComponentResult> components,
    @NotNull @Nonnull String station
) {}