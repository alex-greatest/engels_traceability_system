package com.rena.application.repository.result;

import com.rena.application.entity.model.result.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
}