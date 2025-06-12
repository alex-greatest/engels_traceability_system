package com.rena.application.service.traceability.station.component;

import com.rena.application.entity.dto.traceability.station.component.result.ComponentResultRequest;
import com.rena.application.entity.dto.traceability.station.component.result.ComponentResultResponse;
import com.rena.application.entity.model.settings.component.ComponentType;
import com.rena.application.entity.model.settings.component.ComponentValue;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.traceability.component.ComponentCodeNotEqual;
import com.rena.application.exceptions.traceability.component.ComponentLengthNotEqual;
import com.rena.application.exceptions.traceability.component.ComponentNotFound;
import com.rena.application.repository.settings.component.ComponentTypeRepository;
import com.rena.application.repository.settings.component.ComponentValueRepository;
import com.rena.application.repository.traceability.common.boiler.BoilerRepository;
import com.rena.application.service.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class ComponentResultSaveService {
    private final BoilerRepository boilerRepository;
    private final ComponentValueRepository componentValueRepository;
    private final ComponentTypeRepository componentTypeRepository;
    private final Helper helper;

    @Transactional
    public ComponentResultResponse saveResultsComponent(@Valid ComponentResultRequest componentResultRequest) {
         try {
             return saveComponentValue(componentResultRequest);
         } catch (ComponentNotFound e) {
             log.error("Тип компонента не найден: {}", componentResultRequest.getName(), e);
             return new ComponentResultResponse(null, null, 2, e.getMessage());
         } catch (ComponentLengthNotEqual e) {
             log.error("Длина кода компонента не соответствует типу: {}", componentResultRequest.getName(), e);
             return new ComponentResultResponse(null, null, 2, e.getMessage());
         } catch (ComponentCodeNotEqual e) {
             log.error("Компонент с кодом не совпадает с типом: {}", componentResultRequest.getName(), e);
             return new ComponentResultResponse(null, null, 2, e.getMessage());
         }
    }

    private ComponentResultResponse saveComponentValue(ComponentResultRequest componentResultRequest) {
        var value = componentResultRequest.getValue();
        var boiler = boilerRepository.findBySerialNumber(componentResultRequest.getSerialNumber())
                .orElseThrow(() -> new RecordNotFoundException("Не найден котел с серийным номером: " +
                        componentResultRequest.getSerialNumber()));
        var codeComponent = helper.extractArticleNumber(componentResultRequest.getValue());
        var componentTypeOptional = componentTypeRepository.findByCodeAndName(codeComponent, componentResultRequest.getName());
        var componentType = checkComponentTypeExists(componentTypeOptional);
        checkLengthCode(value, componentType);
        checkComponentType(codeComponent, componentType);
        var valueComponentOld = componentValueRepository.findByCodeNameAndValue(componentType.getCode(), value);
        return valueComponentOld.map(componentValue -> new ComponentResultResponse(null, null, 2,
                        "Компонент уже привязан к котлку: " + componentValue.getBoiler().getSerialNumber())).
                orElseGet(() -> createSuccessComponent(value, boiler, componentType));
    }

    private ComponentType checkComponentTypeExists(Optional<ComponentType> componentType) {
        if (componentType.isEmpty()) {
            throw new RecordNotFoundException("Тип компонента не найден");
        }
        return componentType.get();
    }

    private void checkLengthCode(String value, ComponentType componentType) {
        if (value.length() != componentType.getLengthCode()) {
            throw new ComponentNotFound("Длина кода компонента не соответствует типу: " + componentType.getCode());
        }
    }

    private void checkComponentType(String codeComponent, ComponentType componentType) {
        if (!componentType.getCode().equals(codeComponent)) {
            throw new ComponentCodeNotEqual("Компонент с кодом " + codeComponent + " не совпадает с типом: " + componentType.getCode());
        }
    }

    public ComponentResultResponse createSuccessComponent(String value, Boiler boiler, ComponentType componentType) {
        var componentValue = new ComponentValue();
        componentValue.setValue(value);
        componentValue.setComponentType(componentType);
        componentValue.setBoiler(boiler);
        componentValueRepository.save(componentValue);
        return new ComponentResultResponse(componentType.getName(), componentValue.getValue(), 1, null);
    }
}
