package com.rena.application.repository.settings.component;

import com.rena.application.entity.model.settings.component.ComponentValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ComponentValueRepository extends JpaRepository<ComponentValue, Long> {
    @Query("select c from ComponentValue c join c.boiler join c.componentType where c.componentType.code = ?1 and c.value = ?2")
    Optional<ComponentValue> findByCodeNameAndValue(String code, String value);

    @Transactional
    @Modifying
    @Query("delete from ComponentValue c where c.boiler.serialNumber = ?1")
    void deleteByBoiler(String serialNumber);

    @Query("select c from ComponentValue c join c.boiler join c.componentType where c.boiler.serialNumber = ?1")
    List<ComponentValue> findByBoiler_SerialNumber(String serialNumber);

    @Transactional
    @Modifying
    @Query("update ComponentValue c set c.boiler.serialNumber = ?1 where c.boiler = null")
    void updateBoilerByBoiler(String serialNumber);

    @Query("select c from ComponentValue c join c.componentType join c.boiler where c.componentType.name = ?1 and c.value = ?2 and c.boiler = null")
    Optional<ComponentValue> findByComponentType_NameAndValue(String name, String value);
}