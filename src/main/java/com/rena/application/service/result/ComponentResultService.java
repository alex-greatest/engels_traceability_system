package com.rena.application.service.result;

import com.rena.application.config.mapper.traceability.ComponentMapper;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.traceability.station.components.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentResultService {
    private final ComponentRepository componentRepository;
    private final ComponentMapper componentMapper;
    private final OperationRepository operationRepository;

}