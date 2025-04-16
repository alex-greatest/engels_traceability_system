package com.rena.application.service.print;

import com.rena.application.config.mapper.result.OperationMapper;
import com.rena.application.entity.dto.print.OperationResult;
import com.rena.application.repository.traceability.common.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class OperationResultService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    public List<OperationResult> getOperationsBySerial(String serialNumber) {
        var operations = operationRepository.findAllByBoilerSerialNumber(serialNumber);
        return operationMapper.toDto(operations);
    }
}
