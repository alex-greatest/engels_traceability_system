package com.rena.application.service.traceability.station.order.history;

import com.rena.application.config.mapper.boiler.BoilerMapper;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryRequest;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryResponse;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.boiler.BoilerOrderHistory;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.repository.traceability.common.boiler.BoilerOrderHistoryRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class BoilerOrderHistoryService {
    private final BoilerRepository boilerRepository;
    private final BoilerMapper boilerMapper;
    private final BoilerOrderHistoryRepository boilerOrderHistoryRepository;

    public Page<BoilerHistoryResponse> getBoilers(@Valid BoilerHistoryRequest boilerHistoryRequest) {
        Pageable pageable = PageRequest.of(boilerHistoryRequest.getPage(), boilerHistoryRequest.getSize(),
                Sort.by("dateCreate").ascending());
        var boiler = boilerRepository.findByBoilerOrder_OrderNumber(boilerHistoryRequest.getId(), pageable);
        return boiler.map(boilerMapper::toBoilerResponse);
    }

    public void createBoilerOrderHistory(BoilerOrder boilerOrder, Boiler boiler) {
        var boilerOrderHistory = new BoilerOrderHistory();
        boilerOrderHistory.setBoilerOrder(boilerOrder);
        boilerOrderHistory.setBoiler(boiler);
        boilerOrderHistoryRepository.save(boilerOrderHistory);
    }
}
