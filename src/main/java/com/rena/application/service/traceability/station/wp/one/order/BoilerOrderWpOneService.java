package com.rena.application.service.traceability.station.wp.one.order;

import com.rena.application.entity.dto.traceability.station.wp.one.order.BoilerOrderResponseWpOne;
import com.rena.application.entity.dto.traceability.station.wp.one.order.Canban;
import com.rena.application.entity.dto.traceability.station.wp.one.order.CanbanUniqueCode;
import com.rena.application.entity.model.boiler.type.BoilerTypeCycle;
import com.rena.application.entity.model.traceability.common.Status;
import com.rena.application.entity.model.traceability.station.wp.one.order.BoilerOrder;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeCycleRepository;
import com.rena.application.repository.traceability.station.wp.BoilerOrderRepository;
import com.rena.application.repository.traceability.common.StatusRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.SettingRepository;
import com.rena.application.repository.user.UserHistoryRepository;
import com.rena.application.service.traceability.ErrorService;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Validated
@Service
@Slf4j
public class BoilerOrderWpOneService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerTypeCycleRepository boilerTypeCycleRepository;
    private final UserHistoryRepository userRepository;
    private final StatusRepository statusRepository;
    private final ErrorService errorService;
    private final PartLastRepository partLastRepository;
    private final SettingRepository settingRepository;

    @Transactional
    public BoilerOrderResponseWpOne addOrder(@Valid CanbanUniqueCode canbanUniqueCode) {
        UserHistory user = null;
        try {
            user = userRepository.findByCodeAndIsActive(canbanUniqueCode.userCode(), true).orElseThrow(() ->
                    new RecordNotFoundException("Пользователь не найден"));
            var now = LocalDateTime.now();
            var boilerOrder = getOrder(canbanUniqueCode, now, user);
            checkOrder(boilerOrder);
            var article = boilerOrder.getBoilerTypeCycle() != null ? boilerOrder.getBoilerTypeCycle().getArticle() : "";
            var isDataExists = boilerOrder.getScanCode() != null && !boilerOrder.getScanCode().isEmpty();
            return new BoilerOrderResponseWpOne(boilerOrder.getId(), isDataExists,
                    boilerOrder.getOrderNumber(), article, boilerOrder.getAmountBoilerOrder(),
                    boilerOrder.getAmountBoilerPrint(), "", now);
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            errorSave(user, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private BoilerOrder getOrder(CanbanUniqueCode canbanUniqueCode, LocalDateTime now, UserHistory user) {
        var boilerOrderOptional = boilerOrderRepository.findById(canbanUniqueCode.id());
        return boilerOrderOptional.orElseGet(() -> createOrder(canbanUniqueCode.id(), user, now, canbanUniqueCode.numberShift()));
    }

    private BoilerOrder createOrder(String id, UserHistory user, LocalDateTime now, Integer shiftNumber) {
        var boilerOrderNew = new BoilerOrder();
        boilerOrderNew.setId(id);
        boilerOrderNew.setScanCode("");
        boilerOrderNew.setOrderNumber(0);
        boilerOrderNew.setAmountBoilerOrder(0);
        boilerOrderNew.setAmountBoilerPrint(0);
        boilerOrderNew.setAmountBoilerMade(0);
        boilerOrderNew.setUserHistory(user);
        boilerOrderNew.setModifiedDate(now);
        boilerOrderNew.setNumberShiftCreated(shiftNumber);
        return boilerOrderRepository.save(boilerOrderNew);
    }

    private void checkOrder(BoilerOrder boilerOrder) {
        boolean isOrderReady = boilerOrder.getAmountBoilerPrint() != 0 &&
                boilerOrder.getAmountBoilerPrint() >= boilerOrder.getAmountBoilerOrder();
        if (isOrderReady) {
            partLastRepository.updatePart_idByStation(null, "wp1");
            throw new BoilerOrderReadyNotFoundException("Все этикетки заказа распечатаны");
        }
    }

    private void errorSave(UserHistory userHistory, EndpointException e) {
        if (userHistory != null) {
            errorService.addError(e.getMessage(), "wp1", "Сканирование канбан карты",
                    userHistory, null);
        }
    }

    @Transactional
    public BoilerOrderResponseWpOne updateOrder(@Valid Canban canban) {
        UserHistory user = null;
        try {
            user = userRepository.findByCodeAndIsActive(canban.userCode(), true).orElseThrow(() ->
                    new RecordNotFoundException("Пользователь не найден"));
            var status = statusRepository.getReferenceById(3L);
            var boilerType = boilerTypeCycleRepository.findByArticleAndIsActive(canban.article(), true).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            BoilerOrder boilerOrder = boilerOrderRepository.findById(canban.id()).
                    orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
            checkOrder(boilerOrder);
            updateOrder(canban, boilerOrder, status, boilerType);
            partLastRepository.updatePart_idByStation(boilerOrder.getId(), "wp1");
            return new BoilerOrderResponseWpOne(boilerOrder.getId(), true,
                    boilerOrder.getOrderNumber(), boilerType.getArticle(), boilerOrder.getAmountBoilerOrder(),
                    boilerOrder.getAmountBoilerPrint(), boilerOrder.getScanCode(), boilerOrder.getModifiedDate());
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            errorSave(user, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private void updateOrder(Canban canban, BoilerOrder boilerOrder, Status status, BoilerTypeCycle boilerTypeCycle) {
        boilerOrder.setStatus(status);
        boilerOrder.setBoilerTypeCycle(boilerTypeCycle);
        boilerOrder.setScanCode(canban.code());
        boilerOrder.setOrderNumber(canban.numberOrder());
        boilerOrder.setAmountBoilerOrder(canban.amountBoilerOrder());
    }

    public BoilerOrderResponseWpOne getLastBoilerOrder() {
        var id =  partLastRepository.findByStation_Name("wp1").
                orElseThrow(() -> new RecordNotFoundException("Последний заказ не найден"));
        if (id.getPart_id() != null) {
            var boilerOrder = boilerOrderRepository.findById(id.getPart_id()).
                    orElseThrow(() -> new RecordNotFoundException("Последний заказ не найден"));
            checkOrder(boilerOrder);
            return new BoilerOrderResponseWpOne(boilerOrder.getId(), true,
                    boilerOrder.getOrderNumber(), boilerOrder.getBoilerTypeCycle().getArticle(),
                    boilerOrder.getAmountBoilerOrder(), boilerOrder.getAmountBoilerPrint(),
                    boilerOrder.getScanCode(), boilerOrder.getModifiedDate());
        }
        throw new RecordNotFoundException("Последний заказ не найден");
    }

    public Integer getAmountPrintedBarcode() {
        var setting = settingRepository.findById(1L).
                orElseThrow(() -> new RecordNotFoundException("Настройки не найдены"));
        return setting.getAmountPrintedBarcode();
    }

    public Integer updateAmountPrintedBarcode(Integer amountPrintedBarcode) {
        var setting = settingRepository.findById(1L).
                orElseThrow(() -> new RecordNotFoundException("Настройки не найдены"));
        setting.setAmountPrintedBarcode(amountPrintedBarcode);
        settingRepository.save(setting);
        return setting.getAmountPrintedBarcode();
    }
}
