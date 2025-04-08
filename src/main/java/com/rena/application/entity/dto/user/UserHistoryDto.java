package com.rena.application.entity.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link com.rena.application.entity.model.user.UserHistory}
 */
public record UserHistoryDto(Long id, @NotNull Long userId, @NotNull Integer code,
                             @NotNull @Size(max = 50) String username) {
}