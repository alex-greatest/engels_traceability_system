package com.rena.application.repository.settings.material;

import com.rena.application.entity.model.settings.material.MaterialValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MaterialValueRepository extends JpaRepository<MaterialValue, Long> {

    @Transactional
    @Modifying
    @Query("delete from MaterialValue c where c.boiler.serialNumber = ?1")
    void deleteByBoiler(String serialNumber);

}