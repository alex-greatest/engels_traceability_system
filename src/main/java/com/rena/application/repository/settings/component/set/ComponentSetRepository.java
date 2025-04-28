package com.rena.application.repository.settings.component.set;

import com.rena.application.entity.model.settings.component.set.ComponentSet;
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

    @Query("SELECT c FROM ComponentSet c " +
            "JOIN c.componentType ct " +
            "JOIN c.componentNameSet cns " +
            "JOIN ComponentBinding cb ON cb.componentType.id = ct.id " +
            "JOIN cb.station s " +
            "WHERE cns.id = ?1 " +
            "AND s.name = ?2 " +
            "AND cb.componentNameSet.id = cns.id " +
            "ORDER BY cb.order")
    List<ComponentSet> findAllComponentsByStationOrdered(Long nameSetId, String stationName);

    @Query("select c from ComponentSet c " +
            "JOIN c.componentType ct " +
            "JOIN c.componentNameSet " +
            "JOIN ComponentBinding cb ON cb.componentType.id = ct.id AND cb.componentNameSet.id = c.componentNameSet.id AND cb.station.name = ?3 " +
            "WHERE c.componentNameSet.id = ?1 " +
            "AND c.componentType.id IN ?2 ")
    List<ComponentSet> findAllComponentsByBindingsIdOrdered(Long id, List<Long> idsComponentType, String stationName);
}