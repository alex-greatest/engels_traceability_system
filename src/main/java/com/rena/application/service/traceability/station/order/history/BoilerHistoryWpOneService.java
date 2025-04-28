package com.rena.application.service.traceability.station.order.history;

import com.rena.application.config.mapper.boiler.BoilerMapper;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryRequest;
import com.rena.application.entity.dto.traceability.station.order.history.BoilerHistoryResponse;
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
public class BoilerHistoryWpOneService {
    private final BoilerRepository boilerRepository;
    private final BoilerMapper boilerMapper;

    public Page<BoilerHistoryResponse> getBoilers(@Valid BoilerHistoryRequest boilerHistoryRequest) {
        Pageable pageable = PageRequest.of(boilerHistoryRequest.page(), boilerHistoryRequest.size(),
                Sort.by("dateCreate").ascending());
        var boiler = boilerRepository.findByBoilerOrder_OrderNumber(boilerHistoryRequest.id(), pageable);
        return boiler.map(boilerMapper::toBoilerResponse);
    }
}
