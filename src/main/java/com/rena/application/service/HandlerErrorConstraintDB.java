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
            "fk_boiler_type_on_boiler_type_additional_data_set", "Набор нельзя удалить, он привязан к котлу",
            "fk_boiler_type_on_component_name_set", "Набор нельзя удалить, он привязан к котлу",
            "uc_binding_order", "Компонент с таким порядком уже существует",
            "uc_binding_name_set", "Компонент с таким названием уже существует в наборе",
            "fk_component_set_on_component_type", "Тип компонента нельзя удалить, он привязан к набору");

    public String findMessageError(String message) {
        return CONSTRAINS_MAP.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Ошибка базы данных");
    }
}