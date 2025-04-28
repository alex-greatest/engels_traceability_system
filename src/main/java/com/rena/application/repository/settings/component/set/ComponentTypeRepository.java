package com.rena.application.repository.settings.component.set;

import com.rena.application.entity.model.settings.component.ComponentType;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {
    Optional<ComponentType> findComponentTypeByName(String name);

    @Query("select distinct c.componentType from ComponentBinding c JOIN c.station JOIN " +
            "c.componentNameSet where c.station.name = ?1 and c.componentNameSet.id = ?2 order by c.componentType.name")
    List<ComponentType> findComponentTypesByStationNameAndNameSetId(String nameStation, Long idNameSet);
}