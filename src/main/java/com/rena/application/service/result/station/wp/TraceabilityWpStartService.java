package com.rena.application.service.result.station.wp;

import com.rena.application.config.mapper.component.binding.ComponentBindingMapper;
import com.rena.application.config.mapper.component.set.ComponentSetMapper;
import com.rena.application.entity.dto.result.common.BoilerTypeCycleStation;
import com.rena.application.entity.dto.result.station.wp.components.ComponentRequest;
import com.rena.application.entity.dto.result.station.wp.components.ComponentsResponse;
import com.rena.application.entity.model.boiler.type.BoilerType;
import com.rena.application.entity.model.component.binding.ComponentBinding;
import com.rena.application.entity.model.component.set.ComponentSet;
import com.rena.application.entity.model.result.common.Operation;
import com.rena.application.entity.model.result.common.Boiler;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.result.boiler.*;
import com.rena.application.exceptions.result.component.ComponentsBindingNotFoundException;
import com.rena.application.exceptions.result.component.ComponentsNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeRepository;
import com.rena.application.repository.component.binding.ComponentBindingRepository;
import com.rena.application.repository.component.set.ComponentSetRepository;
import com.rena.application.repository.result.common.OperationRepository;
import com.rena.application.repository.result.common.StationRepository;
import com.rena.application.repository.result.common.StatusRepository;
import com.rena.application.repository.result.common.BoilerRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import com.rena.application.service.result.ErrorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class TraceabilityWpStartService {
    private final BoilerRepository boilerRepository;
    private final BoilerTypeRepository boilerTypeRepository;
    private final OperationRepository operationRepository;
    private final ComponentBindingRepository componentBindingRepository;
    private final ComponentSetRepository componentSetRepository;
    private final ComponentBindingMapper componentBindingMapper;
    private final ComponentSetMapper componentSetMapper;
    private final UserHistoryRepository userHistoryRepository;
    private final StatusRepository statusRepository;
    private final ErrorService errorService;
    private final StationRepository stationRepository;
    private final PartLastRepository partLastRepository;

    @Transactional
    public ComponentsResponse getComponentsBoiler(@Valid ComponentRequest componentRequest) {
        UserHistory user = null;
        Boiler boiler = null;
        try {
            user = userHistoryRepository.findByCodeAndIsActive(componentRequest.userCode(), true).
                    orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
            boiler = boilerRepository.findBySerialNumber(componentRequest.serialNumber()).
                    orElseThrow(() -> new BoilerNotFoundException("Котёл не найден"));
            var boilerType = boilerTypeRepository.findBoilerById(boiler.getBoilerTypeCycle().getBoilerTypeId()).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            var componentsBinding = componentBindingRepository.findByStation_Name(componentRequest.stationName(),
                    boilerType.getComponentNameSet().getId());
            var componentsTypeFromBinding = componentsBinding.stream().map(cb -> cb.getComponentType().getId()).toList();
            var componentsSet = componentSetRepository.findAllComponentsByBindingsId(boilerType.getComponentNameSet().getId(),
                    componentsTypeFromBinding);
            checkComponents(componentsSet, componentsBinding);
            checkPrevStationOperation(componentRequest);
            var prevOperation = operationRepository.findByStation_NameAndIsLastTrue(componentRequest.stationName(), componentRequest.serialNumber());
            operationControl(prevOperation, componentRequest);
            createOperation(componentRequest, user, boiler);
            return createResponse(componentRequest.serialNumber(), boilerType, componentsSet, componentsBinding);
        } catch (BoilerNotFoundException | BoilerTypeNotFoundException | ComponentsBindingNotFoundException |
                 ComponentsNotFoundException e) {
            log.error(e.getMessage(), e);
            errorService.addError(e.getMessage(), componentRequest.stationName(), "Получение списка компонентов", user,
                    boiler != null ? boiler.getSerialNumber() : null);
            throw new RecordNotFoundException(e.getMessage());
        } catch (BoilerTraceabilityOK | BoilerTraceabilityPrevNOK | BoilerPrevStationEmpty e) {
            log.error(e.getMessage(), e);
            errorService.addError(e.getMessage(), componentRequest.stationName(), "Получение списка компонентов", user,
                    boiler != null ? boiler.getSerialNumber() : null);
            throw e;
        }
    }

    private void checkComponents(List<ComponentSet> components, List<ComponentBinding> componentsBinding) {
        if (components.isEmpty()) {
            throw new ComponentsNotFoundException("Компоненты не найдены");
        }
        if (componentsBinding.isEmpty()) {
            throw new ComponentsBindingNotFoundException("Компоненты не привязаны к станции");
        }
    }

    private void operationControl(Optional<Operation> prevOperation,
                                  ComponentRequest componentRequest) {
        var isPrevOperationOK = prevOperation.isEmpty() || !prevOperation.get().getStatus().getName().equals("OK");
        var isAllowStart = isPrevOperationOK || componentRequest.isAllowStart();
        if (isAllowStart) {
            prevOperation.ifPresent(o -> {
                o.setIsLast(false);
                operationRepository.save(o);
            });
            return;
        }
        checkPrevOperationStatus(prevOperation);
    }

    private void checkPrevOperationStatus(Optional<Operation> prevOperation) {
        prevOperation.ifPresent(o -> {
            if (o.getStatus().getName().equals("OK")) {
                throw new BoilerTraceabilityOK("Котёл уже успешно прошёл данную станцию");
            }
        });
    }

    private void checkPrevStationOperation(ComponentRequest componentRequest) {
        if (componentRequest.isAllowStart()) {
            return;
        }
        operationRepository.findByStatus_IdAndStation_NameAndIsLastTrue(1L,
                        componentRequest.prevStationName(),
                        componentRequest.serialNumber()).
                orElseThrow(() -> new BoilerTraceabilityPrevNOK("Котёл не прошёл предыдущую станцию"));
    }

    private void createOperation(ComponentRequest componentRequest, UserHistory userHistory, Boiler boiler) {
        var status = statusRepository.getReferenceById(3L);
        var station = stationRepository.findByName(componentRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        Operation operation = new Operation();
        operation.setDateCreate(LocalDateTime.now());
        operation.setBoiler(boiler);
        operation.setNumberShift(componentRequest.numberShift());
        operation.setStation(station);
        operation.setUserHistory(userHistory);
        operation.setStatus(status);
        operation.setIsLast(true);
        operationRepository.save(operation);
        var partLast = partLastRepository.findByStation_Name(componentRequest.stationName()).
                orElseThrow(() -> new RecordNotFoundException("Станция не найдена"));
        partLast.setPart_id(operation.getId().toString());
    }

    private ComponentsResponse createResponse(String serialNumber,
                                              BoilerType boilerType,
                                              List<ComponentSet> components,
                                              List<ComponentBinding> componentsBinding) {
        var componentsDto = componentSetMapper.toComponentSetDto(components);
        var componentsBindingDto = componentBindingMapper.toDto(componentsBinding);
        var boilerTypeStation = new BoilerTypeCycleStation(boilerType.getTypeName(), boilerType.getArticle(), serialNumber);
        return new ComponentsResponse(boilerTypeStation, componentsDto, componentsBindingDto);
    }

    @Transactional
    public ComponentsResponse getLastOperation(String nameStation) {
        return partLastRepository.findByStation_Name(nameStation).map(partLast -> {
            var operationId = Long.parseLong(partLast.getPart_id());
            return operationRepository.findById(operationId).map(operation -> {
                if (operation.getStatus().getName().equals("IN_PROGRESS")) {
                    var boiler = operation.getBoiler();
                    var boilerType = boilerTypeRepository.findBoilerById(boiler.getBoilerTypeCycle().getBoilerTypeId())
                            .orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
                    var componentsBinding = componentBindingRepository.findByStation_Name(nameStation, boilerType.getComponentNameSet().getId());
                    var componentsTypeFromBinding = componentsBinding.stream().map(cb -> cb.getComponentType().getId()).toList();
                    var componentsSet = componentSetRepository.findAllComponentsByBindingsId(boilerType.getComponentNameSet().getId(),
                            componentsTypeFromBinding);
                    checkComponents(componentsSet, componentsBinding);
                    return createResponse(boiler.getSerialNumber(), boilerType, componentsSet, componentsBinding);
                }
                return null;
            }).orElse(null);
        }).orElse(null);
    }
}
