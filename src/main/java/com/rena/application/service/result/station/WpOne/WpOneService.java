package com.rena.application.service.result.station.WpOne;

import com.rena.application.entity.dto.result.station.WpOneRequest;
import com.rena.application.entity.dto.result.station.WpOneResponse;
import com.rena.application.entity.model.result.common.Boiler;
import com.rena.application.entity.model.result.common.Operation;
import com.rena.application.entity.model.result.common.Station;
import com.rena.application.entity.model.result.common.Status;
import com.rena.application.entity.model.result.order.BoilerOrder;
import com.rena.application.entity.model.user.User;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerOrderNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerTypeNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeRepository;
import com.rena.application.repository.result.common.BoilerRepository;
import com.rena.application.repository.result.common.OperationRepository;
import com.rena.application.repository.result.common.StationRepository;
import com.rena.application.repository.result.common.StatusRepository;
import com.rena.application.repository.result.order.BoilerOrderRepository;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.user.UserRepository;
import com.rena.application.service.result.ErrorService;
import com.rena.application.service.result.helper.WpOneHelper;
import com.rena.application.service.shift.ShiftService;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class WpOneService {
    private final SettingRepository settingRepository;
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerRepository boilerRepository;
    private final StatusRepository statusRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final ErrorService errorService;
    private final OperationRepository operationRepository;
    private final BoilerTypeRepository boilerTypeRepository;
    private final ShiftService shiftService;

    @Transactional
    public WpOneResponse generateBarcodeData(@Valid WpOneRequest wp) {
        User user = null;
        try {
            user = userRepository.findByCode(wp.userCode()).orElseThrow(() -> new RecordNotFoundException("Пользователь не найден"));
            var status = statusRepository.getReferenceById(1L);
            var setting = settingRepository.findById(1L).orElseThrow(() -> new RecordNotFoundException("Настройки не найдены"));
            var boilerOrder = boilerOrderRepository.findByOrderNumber(wp.orderCode()).
                    orElseThrow(() -> new BoilerOrderNotFoundException("Заказ не найден"));
            var boilerType = boilerTypeRepository.findById(boilerOrder.getBoilerTypeId()).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            var serialNumber = WpOneHelper.getSerialNumber(setting, boilerType.getArticle());
            setting.setNextBoilerNumber(setting.getNextBoilerNumber() + 1);
            saveTraceabilityData(user, status, boilerOrder, serialNumber, wp.numberShift());
            var amountBoilerShift = shiftService.updateShiftStation("wp1");
            return new WpOneResponse(serialNumber, boilerOrder.getAmountBoilerPrint(), amountBoilerShift);
        } catch (BoilerTypeNotFoundException | BoilerOrderNotFoundException e) {
            log.error(e.getMessage(), e);
            errorSave(user, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private void saveTraceabilityData(User user, Status status, BoilerOrder boilerOrder, String serialNumber, Integer numberShift) {
        var now = LocalDateTime.now();
        var boiler = createBoiler(boilerOrder, user.getId(), status, serialNumber, now);
        var station = stationRepository.getReferenceById(1L);
        createOperation(boiler, user.getId(), status, station, now, numberShift);
        boilerOrder.setAmountBoilerPrint(boilerOrder.getAmountBoilerPrint() + 1);
        boilerOrderRepository.save(boilerOrder);
    }

    private Boiler createBoiler(BoilerOrder boilerOrder, Long userId, Status status,
                                String serialNumber, LocalDateTime now) {
        var boiler = new Boiler();
        boiler.setBoilerOrder(boilerOrder);
        boiler.setUserId(userId);
        boiler.setStatus(status);
        boiler.setSerialNumber(serialNumber);
        boiler.setBoilerTypeId(boilerOrder.getBoilerTypeId());
        boiler.setDateCreate(now);
        return boilerRepository.save(boiler);
    }

    public void createOperation(Boiler boiler, Long userId, Status status, Station station, LocalDateTime now, Integer numberShift) {
        var operation = new Operation();
        operation.setBoiler(boiler);
        operation.setUserId(userId);
        operation.setDateCreate(now);
        operation.setStatus(status);
        operation.setStation(station);
        operation.setNumberShift(numberShift);
        operationRepository.save(operation);
    }

    private void errorSave(User user, EndpointException e) {
        if (user != null) {
            errorService.addError(e.getMessage(), "wp1", "Печать этикеток", user.getId(), null);
        }
    }
}
