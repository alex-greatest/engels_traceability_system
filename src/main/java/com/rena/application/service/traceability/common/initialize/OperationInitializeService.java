package com.rena.application.service.traceability.common.initialize;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;
import com.rena.application.entity.dto.traceability.station.order.BoilerOrderOperationResponse;
import com.rena.application.entity.model.settings.PartLast;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.service.traceability.station.components.prepare.ComponentsPrepareOperationService;
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
    private final ComponentsPrepareOperationService componentsPrepareOperationService;

    public BoilerMadeInformation getLastMainInformationComponents(String nameStation) {
        return componentsPrepareOperationService.createResponseOperationComponents(nameStation);
    }

    @Transactional
    public RpcBase getLastOperationComponents(@NotBlank String nameStation) {
        var componentsOperationResponse = partLastRepository.findByStation_Name(nameStation).
                map(partLast -> createLastPart(partLast, nameStation)).orElse(null);
        return componentsOperationResponse == null ? getLastMainInformationComponents(nameStation) : componentsOperationResponse;
    }

    public ComponentsOperationStartResponse createLastPart(PartLast partLast, String nameStation) {
        if (partLast.getPart_id() == null) {
            return null;
        }
        var operationId = Long.parseLong(partLast.getPart_id());
        return operationRepository.findById(operationId).
                map(operation -> createResponse(nameStation, operation)).
                orElse(null);
    }

    public ComponentsOperationStartResponse createResponse(String nameStation, Operation operation) {
        if (operation.getStatus() == 3) {
            var boiler = operation.getBoiler();
            return componentsPrepareOperationService.createResponseOperationComponents(boiler, nameStation);
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
