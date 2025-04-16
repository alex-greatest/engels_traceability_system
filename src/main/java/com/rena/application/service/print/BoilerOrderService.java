package com.rena.application.service.print;

import com.rena.application.config.mapper.result.BoilerOrderMapper;
import com.rena.application.entity.dto.print.BoilerOrderDto;
import com.rena.application.entity.model.traceability.station.wp.one.order.BoilerOrder;
import com.rena.application.repository.traceability.station.wp.BoilerOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
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