package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BoilerTypeAdditionalValueHistoryRepository extends JpaRepository<BoilerTypeAdditionalValueHistory, Long> {
    Optional<BoilerTypeAdditionalValueHistory> findByBoilerTypeAdditionalValueIdAndIsActive(Long boilerTypeAdditionalValueId, Boolean isActive);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO boiler_type_additional_value_history (
                boiler_type_additional_value_id,
                value,
                old_value,
                unit,
                boiler_type_additional_data_id,
                boiler_type_additional_data_set_id,
                modified_date,
                type_operation,
                is_active,
                user_history_id
            )
            SELECT
                btav.id AS boiler_type_additional_value_id,  -- ID из основной таблицы
                btav.value,                                  -- текущее значение
                '' AS old_value,                           -- старое значение (для INSERT оно NULL)
                btav.unit,                                   -- единицы измерения
                btav.boiler_type_additional_data_id,         -- ID параметра
                ?2,                                         -- ваш boiler_type_additional_data_set_history_id (замените)
                ?3,                      -- текущая дата и время
                1 AS type_operation,                  -- тип операции (INSERT/UPDATE/DELETE)
                TRUE AS is_active,                           -- флаг активности
                ?1                                           -- ваш user_history_id (замените)
            FROM boiler_type_additional_value btav
                     JOIN boiler_type_additional_data bad
                          ON btav.boiler_type_additional_data_id = bad.id
                     JOIN boiler_type_additional_data_template bat
                          ON bad.id = bat.boiler_type_additional_data_id
            WHERE btav.boiler_type_additional_data_set_id = ?2;  -- ваш set_id из основной таблицы""",
            nativeQuery = true)
    void addAdditionalValueHistory(Long userIdHistory,
                                   Long boilerTypeAdditionDataSetId,
                                   LocalDateTime now);
}