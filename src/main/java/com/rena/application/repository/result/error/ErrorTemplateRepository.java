package com.rena.application.repository.result.error;

import com.rena.application.entity.model.result.error.ErrorTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ErrorTemplateRepository extends JpaRepository<ErrorTemplate, Long> {
    @Query("select e from ErrorTemplate e JOIN e.station JOIN e.step where e.message = ?1 and e.station.name = ?2")
    Optional<ErrorTemplate> findByMessageAndStation_Name(String message, String name);
}