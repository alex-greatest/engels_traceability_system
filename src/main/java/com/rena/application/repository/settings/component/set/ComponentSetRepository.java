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

    /*@Query(value = "SELECT DISTINCT ct.name FROM component_set c " +
            "JOIN component_type ct ON c.component_type_id = ct.id " +
            "JOIN component_name_set cns ON c.component_name_set_id = cns.id " +
            "JOIN component_binding cb ON cb.component_type_id = ct.id " +
            "JOIN station s ON cb.station_id = s.id " +
            "WHERE cns.id = ?1 " +
            "AND s.name = ?2 " +
            "AND cb.component_name_set_id = cns.id " +
            "ORDER BY cb.order_component", nativeQuery = true)
    List<String> findDistinctComponentTypeNamesByStationAndNameSet(Long nameSetId, String stationName);*/


    @Query(value = "SELECT ct.name FROM component_set c " +
            "JOIN component_type ct ON c.component_type_id = ct.id " +
            "JOIN component_name_set cns ON c.component_name_set_id = cns.id " +
            "JOIN component_binding cb ON cb.component_type_id = ct.id " +
            "JOIN station s ON cb.station_id = s.id " +
            "WHERE cns.id = ?1 " +
            "AND s.name = ?2 " +
            "AND cb.component_name_set_id = cns.id " +
            "GROUP BY ct.name " +
            "ORDER BY MIN(cb.order_component)", nativeQuery = true)
    List<String> findDistinctComponentTypeNamesByStationAndNameSet(Long nameSetId, String stationName);
}