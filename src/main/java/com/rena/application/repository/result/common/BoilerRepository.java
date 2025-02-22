package com.rena.application.repository.result.common;

import com.rena.application.entity.model.result.common.Boiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BoilerRepository extends JpaRepository<Boiler, Long> {
    @Query("select b from Boiler b where b.serialNumber = ?1 and b.status.name = ?2")
    List<Boiler> findBySerialNumberAndStatus_Name(Integer orderNumber);
}