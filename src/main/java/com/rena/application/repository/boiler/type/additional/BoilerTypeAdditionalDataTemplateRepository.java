package com.rena.application.repository.boiler.type.additional;

import com.rena.application.entity.model.boiler.BoilerTypeAdditionalDataTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BoilerTypeAdditionalDataTemplateRepository extends JpaRepository<BoilerTypeAdditionalDataTemplate, Long> {
    @Query("""
            select b from BoilerTypeAdditionalDataTemplate b JOIN b.boilerTypeAdditionalData""")
    List<BoilerTypeAdditionalDataTemplate> findAllTemplate();
}