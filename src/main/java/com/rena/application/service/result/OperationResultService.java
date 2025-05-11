package com.rena.application.service.result;

import com.rena.application.entity.dto.result.OperationResult;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class OperationResultService {
    private final OperationRepository operationRepository;
}
