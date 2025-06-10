package com.rena.application.repository.settings.component;

import com.rena.application.entity.model.settings.component.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {
    @Query("select c from ComponentType c where c.code = ?1")
    Optional<ComponentType> findByCode(String code);
}