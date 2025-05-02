package com.rena.application.service.traceability.station.initialize;

import com.rena.application.entity.dto.traceability.common.exchange.MainInformation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.entity.dto.traceability.station.order.BoilerOrderOperationResponse;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.service.traceability.station.components.prepare.ComponentsResponserOperationService;
import com.rena.application.service.traceability.station.order.BoilerOrderHelperService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OperationInitializeService {
    private final PartLastRepository partLastRepository;
    private final OperationRepository operationRepository;
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerOrderHelperService boilerOrderHelperService;
    private final ComponentsResponserOperationService componentsResponserOperationService;

    public MainInformation getLastMainInformationComponents(String nameStation) {
        return componentsResponserOperationService.createResponseOperationComponents(nameStation);
    }

    @Transactional
    public RpcBase getLastOperationComponents(@NotBlank String nameStation) {
        var componentsOperationResponse = partLastRepository.findByStation_Name(nameStation).map(partLast -> {
            var operationId = Long.parseLong(partLast.getPart_id());
            return operationRepository.findById(operationId).
                    map(operation -> createResponse(nameStation, operation)).
                    orElse(null);
        }).orElse(null);
        return componentsOperationResponse == null ? getLastMainInformationComponents(nameStation) : componentsOperationResponse;
    }

    public ComponentsOperationStartResponse createResponse(String nameStation, Operation operation) {
        if (operation.getStatus() == 4) {
            var boiler = operation.getBoiler();
            return componentsResponserOperationService.createResponseOperationComponents(boiler, nameStation);
        }
        return null;
    }

    public BoilerOrderOperationResponse getLastBoilerOrder() {
        var id =  partLastRepository.findByStation_Name("wp1").
                orElseThrow(() -> new RecordNotFoundException("Последний заказ не найден"));
        if (id.getPart_id() != null) {
            var boilerOrder = boilerOrderRepository.findById(id.getPart_id()).
                    orElseThrow(() -> new RecordNotFoundException("Последний заказ не найден"));
            boilerOrderHelperService.checkOrder(boilerOrder);
            return new BoilerOrderOperationResponse(boilerOrder.getId(), true,
                    boilerOrder.getOrderNumber(), boilerOrder.getBoilerTypeCycle().getArticle(),
                    boilerOrder.getAmountBoilerOrder(), boilerOrder.getAmountBoilerPrint(),
                    boilerOrder.getScanCode(), boilerOrder.getModifiedDate());
        }
        throw new RecordNotFoundException("Последний заказ не найден");
    }
}
