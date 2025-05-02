package com.rena.application.service.result;

import com.rena.application.config.mapper.traceability.ComponentMapper;
import com.rena.application.entity.dto.result.ComponentsResults;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.result.ComponentRepository;
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

    public ComponentsResults getComponentsByOperationId(Long operationId) {
        var components = componentRepository.findByOperation_IdOrderByNameAsc(operationId);
        var operation = operationRepository.findOperationById(operationId).
                orElseThrow(() -> new RecordNotFoundException("Операция не найдена"));
        var componentResults = componentMapper.toComponentResult(components);
        return new ComponentsResults(componentResults, operation.getStation().getDescription());
    }
}