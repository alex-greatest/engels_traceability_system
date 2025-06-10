package com.rena.application.repository.settings.component;

import com.rena.application.entity.model.settings.component.ComponentType;
import com.rena.application.entity.model.settings.component.ComponentBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ComponentBindingRepository extends JpaRepository<ComponentBinding, Long> {
    @Transactional
    @Modifying
    @Query("delete from ComponentBinding c where c.componentType = ?1")
    void deleteByComponentType(ComponentType componentType);

    @Query("select c.componentType from ComponentBinding c JOIN c.station JOIN " +
            "c.boilerType JOIN c.componentType where c.station.name = ?1 and c.boilerType.id = ?2 order by c.order")
    List<ComponentType> findByStation_Name(String nameStation, Long boilerTypeId);
}