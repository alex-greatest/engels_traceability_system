package com.rena.application.repository.boiler;

import com.rena.application.entity.model.boiler.Boiler;
import com.rena.application.entity.model.component.ComponentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface BoilerRepository extends JpaRepository<Boiler, Long> {
    @Query("select b from Boiler b JOIN b.componentNameSet")
    List<ComponentSet> findAllBoilers();

    @Query("select b from Boiler b JOIN b.componentNameSet where b.id = ?1")
    Optional<Boiler> findBoilerById(Long id);
}