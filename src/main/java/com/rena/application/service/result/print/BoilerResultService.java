package com.rena.application.service.result.print;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.rena.application.config.mapper.boiler.BoilerMapper;
import com.rena.application.entity.dto.result.print.BoilerResult;
import com.rena.application.entity.model.result.common.Boiler;
import com.rena.application.repository.result.common.BoilerRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BoilerResultService {
    private final BoilerRepository boilerRepository;
    private final BoilerMapper boilerMapper;

    public List<BoilerResult> getAllBoilers() {
        List<Boiler> boilers = boilerRepository.findAll();
        return boilerMapper.toDtoBoilerResult(boilers);
    }

    public List<BoilerResult> getBoilersByDateCreateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Boiler> boilers = boilerRepository.findAllByDateCreateBetween(startDate, endDate);
        return boilerMapper.toDtoBoilerResult(boilers);
    }

    public List<BoilerResult> getBoilersById(Long boilerId) {
        List<Boiler> boilers = boilerRepository.findAllByBoilerOrderId(boilerId);
        return boilerMapper.toDtoBoilerResult(boilers);
    }
}
