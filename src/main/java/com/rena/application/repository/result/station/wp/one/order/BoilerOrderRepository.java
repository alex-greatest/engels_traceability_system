package com.rena.application.repository.result.station.wp.one.order;

import com.rena.application.entity.model.result.station.wp.one.order.BoilerOrder;
import com.rena.application.entity.sql.BoilerOrderSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface BoilerOrderRepository extends JpaRepository<BoilerOrder, Long> {
    @Query("select b from BoilerOrder b JOIN b.boilerTypeCycle join b.userHistory where b.orderNumber = ?1")
    Optional<BoilerOrder> findByOrderNumber(Integer orderNumber);

    @Query("select b from BoilerOrder b left JOIN b.boilerTypeCycle where b.id = ?1")
    Optional<BoilerOrder> findById(String id);

    @Query(value = "SELECT amount_boiler_made, amount_boiler_order, amount_boiler_print, boiler_type_id, article " +
            "FROM boiler_order JOIN public.boiler_type bt " +
            "ON boiler_order.boiler_type_id = bt.id " +
            "WHERE order_number = ?1", nativeQuery = true)
    Optional<BoilerOrderSQL> findByOrderNumberSQL(Integer orderNumber);
}