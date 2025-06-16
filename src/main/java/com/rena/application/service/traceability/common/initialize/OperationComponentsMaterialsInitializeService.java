package com.rena.application.service.traceability.common.initialize;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.settings.PartLast;
import com.rena.application.entity.model.traceability.common.Operation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.entity.model.traceability.common.station.StationHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.component.ComponentValueRepository;
import com.rena.application.repository.settings.material.MaterialValueRepository;
import com.rena.application.repository.traceability.common.router.StationHistoryRepository;
import com.rena.application.repository.traceability.common.station.OperationRepository;
import com.rena.application.service.traceability.station.component.prepare.ComponentsPrepareOperationService;
import com.rena.application.service.traceability.station.material.prepare.MaterialsPrepareOperationService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OperationComponentsMaterialsInitializeService {
    private final PartLastRepository partLastRepository;
    private final OperationRepository operationRepository;
    private final ComponentsPrepareOperationService componentsPrepareOperationService;
    private final MaterialsPrepareOperationService materialsPrepareOperationService;
    private final StationHistoryRepository stationHistoryRepository;
    private final ComponentValueRepository componentValueRepository;
    private final MaterialValueRepository materialValueRepository;
    private final MainInformationService mainInformationService;

    public BoilerMadeInformation getLastMainInformationComponents() {
        return mainInformationService.getBoilerMadeInfo();
    }

    @Transactional
    public RpcBase getLastOperation(@NotBlank String nameStation) {
        return partLastRepository.findByStation_Name(nameStation).
                map(partLast -> createLastPart(partLast, nameStation)).
                orElseGet(this::getLastMainInformationComponents);
    }

    private RpcBase createLastPart(PartLast partLast, String nameStation) {
        if (partLast.getPart_id() == null || partLast.getPart_id().trim().isEmpty()) {
            return null;
        }
        var operationId = Long.parseLong(partLast.getPart_id());
        return operationRepository.findById(operationId).
                map(operation -> createResponse(nameStation, operation)).
                orElse(null);
    }

    private RpcBase createResponse(String nameStation, Operation operation) {
        if (operation.getStatus() == 3) {
            var boiler = operation.getBoiler();
            var station = stationHistoryRepository.findByName(nameStation).
                    orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
            return switch (station.getStationType().getName()) {
                case "Компоненты" -> getLastComponents(boiler, station);
                case "Материалы" -> getLastMaterials(boiler, station);
                default -> throw new RecordNotFoundException("Тип станции не найден");
            };
        }
        return null;
    }

    private RpcBase getLastComponents(Boiler boiler, StationHistory station) {
        componentValueRepository.deleteByBoiler(boiler.getSerialNumber());
        return componentsPrepareOperationService.createResponseOperationComponents(boiler, station);
    }

    private RpcBase getLastMaterials(Boiler boiler, StationHistory station) {
        materialValueRepository.deleteByBoiler(boiler.getSerialNumber());
        return materialsPrepareOperationService.createResponseOperationMaterials(boiler, station);
    }
}
