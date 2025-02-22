package com.rena.application.repository.result.order;

import com.rena.application.entity.model.result.order.BoilerOrder;
import com.rena.application.entity.sql.BoilerOrderSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface BoilerOrderRepository extends JpaRepository<BoilerOrder, Long> {
    @Query(value = "SELECT amount_boiler_made, amount_boiler_order, amount_boiler_print, boiler_type_id, article " +
            "FROM boiler_order JOIN public.boiler_type bt " +
            "ON boiler_order.boiler_type_id = bt.id " +
            "WHERE order_number = ?1", nativeQuery = true)
    Optional<BoilerOrderSQL> findByOrderNumberSQL(Integer orderNumber);

    Optional<BoilerOrder> findByOrderNumber(Integer orderNumber);
}