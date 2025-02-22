package com.rena.application.repository.result.error;

import com.rena.application.entity.model.result.error.ErrorTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ErrorTemplateRepository extends JpaRepository<ErrorTemplate, Long> {
    @Query("select e from ErrorTemplate e JOIN e.station JOIN e.errorMessage JOIN e.errorStep " +
            "where e.station.name = ?1 and e.errorMessage.name = ?2 and e.errorStep.name = ?3")
    Optional<ErrorTemplate> findByStation_NameAndErrorMessage_Name(String station, String errorMessage, String step);
}