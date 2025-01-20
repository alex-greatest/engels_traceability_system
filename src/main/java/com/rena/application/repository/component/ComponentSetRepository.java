package com.rena.application.repository.component;

import com.rena.application.entity.model.component.ComponentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
}