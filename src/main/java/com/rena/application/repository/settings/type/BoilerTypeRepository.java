package com.rena.application.repository.settings.type;

import com.rena.application.entity.model.settings.type.BoilerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface BoilerTypeRepository extends JpaRepository<BoilerType, Long> {
    @Query("select b from BoilerType b JOIN b.componentNameSet")
    List<BoilerType> findAllBoilers();

    @Query("select b from BoilerType b JOIN b.componentNameSet where b.id = ?1")
    Optional<BoilerType> findBoilerById(Long id);
}