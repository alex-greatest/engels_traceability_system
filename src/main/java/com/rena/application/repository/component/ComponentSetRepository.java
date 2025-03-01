package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.id = ?1")
    Optional<ComponentSet> findByIdComponentSet(Long id);

    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentNameSet.id = ?1")
    List<ComponentSet> findAllComponentsById(Long id);
}