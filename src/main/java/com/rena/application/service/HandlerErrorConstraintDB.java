package com.rena.application.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Getter
public class HandlerErrorConstraintDB {
    private final Map<String, String> CONSTRAINS_MAP = Map.of(
            "uc_user__code", "Пользователь с таким кодом уже существует",
            "uc_user__username", "Пользователь с таким именем уже существует",
            "uc_component_name_set_name", "Набор компонентов с таким именем уже существует",
            "uc_component_name", "Компонент с таким именем уже существует",
            "uc_boiler_type_additional_data_set", "Набор данных котла с таким именем уже существует",
            "UQ_reference_recipe_reference_id_recipe_id", "Тег для данного типа уже добавлен",
            "UQ_recipe_path_tag_unique", "Тег с таким именем уже существует",
            "UQ_reference_tag_result_reference_id_tag_result_id", "Тег с таким именем уже существует",
            "UQ_tag_result_unique", "Тег с таким именем уже существует");

    public String findMessageError(String message) {
        return CONSTRAINS_MAP.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Ошибка базы данных");
    }
}
