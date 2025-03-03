package com.rena.application.service.result.station.wp.one;

import com.rena.application.config.mapper.boiler.BoilerMapper;
import com.rena.application.entity.dto.result.station.wp.one.boiler.BoilerRequestWpOne;
import com.rena.application.entity.dto.result.station.wp.one.boiler.BoilerResponseWpOne;
import com.rena.application.repository.result.station.wp.one.order.BoilerRepository;
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
public class BoilerWpOneService {
    private final BoilerRepository boilerRepository;
    private final BoilerMapper boilerMapper;

    public Page<BoilerResponseWpOne> getBoilers(@Valid BoilerRequestWpOne boilerRequestWpOne) {
        Pageable pageable = PageRequest.of(boilerRequestWpOne.page(), boilerRequestWpOne.size(),
                Sort.by("dateCreate").ascending());
        var boiler = boilerRepository.findByBoilerOrder_OrderNumber(boilerRequestWpOne.id(), pageable);
        return boiler.map(boilerMapper::toBoilerResponse);
    }
}
