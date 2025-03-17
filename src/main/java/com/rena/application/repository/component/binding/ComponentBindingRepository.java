package com.rena.application.repository.component.binding;

import com.rena.application.entity.model.component.ComponentType;
import com.rena.application.entity.model.component.binding.ComponentBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ComponentBindingRepository extends JpaRepository<ComponentBinding, Long> {
    @Transactional
    @Modifying
    @Query("delete from ComponentBinding c where c.componentType = ?1")
    void deleteByComponentType(ComponentType componentType);

    @Query("select c from ComponentBinding c JOIN c.station JOIN " +
            "c.componentNameSet JOIN c.componentType where c.station.name = ?1 and c.componentNameSet.id = ?2 order by c.order")
    List<ComponentBinding> findByStation_Name(String nameStation, Long idNameSet);

    @Query("select c from ComponentBinding c JOIN c.station JOIN " +
            "c.componentNameSet JOIN c.componentType where c.id = ?1")
    Optional<ComponentBinding> findBindingById(Long id);
}