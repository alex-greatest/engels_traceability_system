package com.rena.application.repository.component.set;

import com.rena.application.entity.model.component.set.ComponentSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.id = ?1")
    Optional<ComponentSet> findByIdComponentSet(Long id);

    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentNameSet.name = ?1")
    Optional<ComponentSet> findByIdComponentNameSet(String nameSet);

    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentNameSet.id = ?1")
    List<ComponentSet> findAllComponentsById(Long id);

    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentNameSet.id = ?1 " +
            "and c.componentType.id in ?2 ORDER BY c.componentType.name")
    List<ComponentSet> findAllComponentsByBindingsId(Long id, List<Long> idsComponentType);

    @Query("select distinct(c.componentType.name) from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentNameSet.id = ?1")
    List<String> findAllDistinctComponentsById(Long id);

    @Query("select c from ComponentSet c JOIN c.componentType JOIN c.componentNameSet where c.componentType.name = ?1")
    List<ComponentSet> findAllComponentsByTypeName(String name);
}