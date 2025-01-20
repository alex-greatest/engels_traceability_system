package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
    @Query("select c from ComponentSet c JOIN c.component JOIN c.componentNameSet where c.id = ?1")
    List<ComponentSet> findByIdComponentSet(Long id);

    @Query("select c from ComponentSet c JOIN c.component JOIN c.componentNameSet where c.id = ?1")
    List<ComponentSet> findAllComponentsById(Long id);
}