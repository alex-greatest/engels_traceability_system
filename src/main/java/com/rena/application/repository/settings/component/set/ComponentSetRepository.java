package com.rena.application.repository.settings.component.set;

import com.rena.application.entity.model.settings.component.set.ComponentSet;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@JaversSpringDataAuditable
public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
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

    @Query("SELECT DISTINCT ct.name FROM ComponentSet c " +
            "JOIN c.componentType ct " +
            "JOIN c.componentNameSet cns " +
            "JOIN ComponentBinding cb ON cb.componentType.id = ct.id " +
            "JOIN cb.station s " +
            "WHERE cns.id = ?1 " +
            "AND s.name = ?2 " +
            "AND cb.componentNameSet.id = cns.id " +
            "ORDER BY ct.name")
    List<String> findDistinctComponentTypeNamesByStationAndNameSet(Long nameSetId, String stationName);
}