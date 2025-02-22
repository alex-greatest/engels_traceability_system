package com.rena.application.service.result.order;

import com.rena.application.entity.dto.result.order.BoilerOrderResponse;
import com.rena.application.entity.dto.result.order.Canban;
import com.rena.application.entity.model.boiler.type.BoilerType;
import com.rena.application.entity.model.result.common.Status;
import com.rena.application.entity.model.result.order.BoilerOrder;
import com.rena.application.entity.model.user.User;
import com.rena.application.exceptions.result.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.exceptions.result.boiler.BoilerTypeNotFoundException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.type.BoilerTypeRepository;
import com.rena.application.repository.result.order.BoilerOrderRepository;
import com.rena.application.repository.result.common.StatusRepository;
import com.rena.application.repository.user.UserRepository;
import com.rena.application.service.result.ErrorService;
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
    public BoilerOrderResponse addOrder(@Valid Canban canban) {
        User user = null;
        try {
            user = userRepository.findByCode(canban.userCode()).orElseThrow(() ->
                    new RecordNotFoundException("Пользователь не найден"));
            var status = statusRepository.getReferenceById(3L);
            var boilerType = boilerTypeRepository.findByArticle(canban.article()).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            User finalUser = user;
            com.rena.application.entity.model.result.order.BoilerOrder boilerOrder = boilerOrderRepository.
                    findByOrderNumber(canban.numberOrder()).orElseGet(() -> createOrder(boilerType, finalUser, status, canban));
            checkOrder(boilerOrder);
            return new BoilerOrderResponse(boilerOrder.getOrderNumber(), boilerType.getArticle(),
                    boilerOrder.getAmountBoilerOrder(), boilerOrder.getAmountBoilerPrint(),
                    boilerOrder.getScanCode(), canban.dateScan());
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            errorSave(user, e);
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    private com.rena.application.entity.model.result.order.BoilerOrder createOrder(BoilerType boilerType, User user, Status status, Canban canban) {
        var boilerOrderNew = new com.rena.application.entity.model.result.order.BoilerOrder();
        boilerOrderNew.setOrderNumber(canban.numberOrder());
        boilerOrderNew.setAmountBoilerOrder(canban.amountBoilerOrder());
        boilerOrderNew.setStatus(status);
        boilerOrderNew.setAmountBoilerPrint(0);
        boilerOrderNew.setAmountBoilerMade(0);
        boilerOrderNew.setUserId(user.getId());
        boilerOrderNew.setScanCode(canban.code());
        boilerOrderNew.setModifiedDate(canban.dateScan());
        boilerOrderNew.setBoilerTypeId(boilerType.getId());
        boilerOrderNew.setNumberShiftCreated(canban.numberShift());
        return boilerOrderRepository.save(boilerOrderNew);
    }

    private void checkOrder(BoilerOrder boilerOrderResponse) {
        if (Objects.equals(boilerOrderResponse.getAmountBoilerOrder(), boilerOrderResponse.getAmountBoilerPrint())) {
            throw new BoilerOrderReadyNotFoundException("Все этикетки заказа распечатаны");
        }
    }

    private void errorSave(User user, EndpointException e) {
        if (user != null) {
            errorService.addError(e.getMessage(), "wp1", "Сканирование канбан карты",
                    user.getId(), null);
        }
    }
}
