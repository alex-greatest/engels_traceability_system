package com.rena.application.service.result.print;

import com.rena.application.config.mapper.result.BoilerOrderMapper;
import com.rena.application.entity.dto.result.print.BoilerOrderDto;
import com.rena.application.entity.model.result.station.wp.one.order.BoilerOrder;
import com.rena.application.repository.result.station.wp.one.order.BoilerOrderRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class BoilerOrderService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerOrderMapper boilerOrderMapper;

    public List<BoilerOrderDto> getAllBoilerOrders() {
        List<BoilerOrder> boilerOrders = boilerOrderRepository.findAll();
        return boilerOrderMapper.toDto(boilerOrders);
    }

    public List<BoilerOrderDto> getBoilerOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<BoilerOrder> boilerOrders = boilerOrderRepository.findByModifiedDateBetween(startDate, endDate);
        return boilerOrderMapper.toDto(boilerOrders);
    }
}