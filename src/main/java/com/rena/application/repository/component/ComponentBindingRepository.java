package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ComponentBindingRepository extends JpaRepository<ComponentBinding, Long> {
    @Query("select c from ComponentBinding c where c.station.name = ?1")
    List<ComponentBinding> findByStation_Name(String name);
}