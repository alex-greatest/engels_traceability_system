package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalValue;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
public interface BoilerTypeAdditionalValueRepository extends JpaRepository<BoilerTypeAdditionalValue, Long> {
    @Modifying
    @Query(value = """
            INSERT INTO boiler_type_additional_value (value, unit, boiler_type_additional_data_id, boiler_type_additional_data_set_id)
                SELECT '', bat.unit, bad.id, ?1 FROM boiler_type_additional_data bad
                JOIN boiler_type_additional_data_template bat ON bad.id = bat.boiler_type_additional_data_id;""",
            nativeQuery = true)
    void addAdditionalValue(Long boilerAdditionalSetId);

    @Query(value = """
           SELECT b from BoilerTypeAdditionalValue b
           JOIN b.boilerTypeAdditionalDataSet
           JOIN b.boilerTypeAdditionalData where b.boilerTypeAdditionalDataSet.id = ?1""")
    List<BoilerTypeAdditionalValue> findAllBoilerTypeAdditionalValue(Long boilerTypeAdditionalDataSetId);

    @Query(value = """
           SELECT b from BoilerTypeAdditionalValue b
           JOIN b.boilerTypeAdditionalDataSet
           JOIN b.boilerTypeAdditionalData where b.id = ?1""")
    Optional<BoilerTypeAdditionalValue> findBoilerTypeAdditionalValueById(Long id);
}