package com.rena.application.repository.result;

import com.rena.application.entity.model.result.order.BoilerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoilerOrderRepository extends JpaRepository<BoilerOrder, Long> {
    Optional<BoilerOrder> findByOrderNumber(Long orderNumber);
}