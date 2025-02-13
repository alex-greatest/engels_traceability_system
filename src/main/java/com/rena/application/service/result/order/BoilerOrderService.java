package com.rena.application.service.result.order;

import com.rena.application.entity.dto.result.order.Canban;
import com.rena.application.entity.model.boiler.type.BoilerType;
import com.rena.application.entity.model.result.Status;
import com.rena.application.entity.model.result.order.BoilerOrder;
import com.rena.application.entity.model.user.User;
import com.rena.application.exceptions.result.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerTypeNotFoundException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.result.StatusRepository;
import com.rena.application.repository.user.UserRepository;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.Objects;

@RequiredArgsConstructor
@Validated
@Service
@Slf4j
public class BoilerOrderService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerTypeRepository boilerTypeRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final ErrorService errorService;

    @Transactional
    public BoilerOrder addOrder(@Valid Canban canban) {
        User user = null;
        BoilerType boilerType = null;
        try {
            user = userRepository.findByCode(canban.userCode()).orElseThrow(() ->
                    new RecordNotFoundException("Пользователь не найден"));
            var status = statusRepository.findByName("IN_PROGRESS").orElseThrow(() ->
                    new RecordNotFoundException("Статус не найден"));
            boilerType = boilerTypeRepository.findByArticle(canban.article()).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            BoilerType finalBoilerType = boilerType;
            User finalUser = user;
            BoilerOrder boilerOrder = boilerOrderRepository.findByOrderNumber(canban.numberOrder()).
                    orElseGet(() -> createOrder(finalBoilerType, finalUser, status, canban));
            checkOrder(boilerOrder);
            return boilerOrder;
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            log.error(e.getMessage(), e);
            errorSave(user, boilerType, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private BoilerOrder createOrder(BoilerType boilerType, User user, Status status, Canban canban) {
        var boilerOrderNew = new BoilerOrder();
        boilerOrderNew.setOrderNumber(canban.numberOrder());
        boilerOrderNew.setAmountBoiler(canban.amountBoiler());
        boilerOrderNew.setStatus(status);
        boilerOrderNew.setAmountBoilerPrint(0);
        boilerOrderNew.setAmountBoilerMade(0);
        boilerOrderNew.setUserId(user.getId());
        boilerOrderNew.setScan_code(canban.code());
        boilerOrderNew.setModifiedDate(canban.dateScan());
        boilerOrderNew.setBoilerTypeId(boilerType.getId());
        return boilerOrderRepository.save(boilerOrderNew);
    }

    private void checkOrder(BoilerOrder boilerOrder) {
        if (Objects.equals(boilerOrder.getAmountBoiler(), boilerOrder.getAmountBoilerPrint())) {
            throw new BoilerOrderReadyNotFoundException("Все этикетки заказа распечатаны");
        }
    }

    private void errorSave(User user, BoilerType boilerType, EndpointException e) {
        if (user != null && boilerType != null) {
            errorService.addError(e.getMessage(), "Рабочее место 1", user.getId(), boilerType.getId());
        }
    }
}
