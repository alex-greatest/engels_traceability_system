package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.BoilerTypeAdditionalValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoilerTypeAdditionalValueRepository extends JpaRepository<BoilerTypeAdditionalValue, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO boiler_type_additional_value (value, unit, boiler_type_additional_data_id, boiler_type_additional_data_set_id)
                SELECT '', bat.unit, bad.id, ?1 FROM boiler_type_additional_data bad
                JOIN boiler_type_additional_data_template bat ON bad.id = bat.boiler_type_additional_data_id;""",
            nativeQuery = true)
    void addAdditionalValue(Long boilerAdditionalSetId);
}