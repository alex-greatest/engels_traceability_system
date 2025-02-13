package com.rena.application.repository.result;

import com.rena.application.entity.model.result.Boiler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoilerRepository extends JpaRepository<Boiler, Long> {
}