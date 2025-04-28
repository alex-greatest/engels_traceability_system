package com.rena.application.service.traceability.station.order;

import com.rena.application.entity.dto.traceability.station.order.BoilerOrderOperationResponse;
import com.rena.application.entity.dto.traceability.station.order.canban.Canban;
import com.rena.application.entity.dto.traceability.station.order.canban.CanbanUniqueCode;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.rena.application.exceptions.traceability.boiler.BoilerOrderReadyNotFoundException;
import com.rena.application.exceptions.traceability.boiler.BoilerTypeNotFoundException;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.settings.type.BoilerTypeCycleRepository;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.repository.settings.user.UserHistoryRepository;
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
public class BoilerOrderManageService {
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerTypeCycleRepository boilerTypeCycleRepository;
    private final UserHistoryRepository userRepository;
    private final PartLastRepository partLastRepository;
    private final BoilerOrderHelperService boilerOrderHelperService;

    public BoilerOrder getBoilerOrder(String id) {
        return boilerOrderRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Заказ не найден"));
    }

    @Transactional
    public BoilerOrderOperationResponse addOrder(@Valid CanbanUniqueCode canbanUniqueCode) {
        try {
            var user = userRepository.findByCodeAndIsActive(canbanUniqueCode.userCode(), true).orElseThrow(() ->
                    new RecordNotFoundException("Пользователь не найден"));
            var now = LocalDateTime.now();
            var boilerOrder = boilerOrderHelperService.getOrder(canbanUniqueCode, now, user);
            boilerOrderHelperService.checkOrder(boilerOrder);
            var article = boilerOrder.getBoilerTypeCycle() != null ? boilerOrder.getBoilerTypeCycle().getArticle() : "";
            var isDataExists = boilerOrder.getScanCode() != null && !boilerOrder.getScanCode().isEmpty();
            return new BoilerOrderOperationResponse(boilerOrder.getId(), isDataExists,
                    boilerOrder.getOrderNumber(), article, boilerOrder.getAmountBoilerOrder(),
                    boilerOrder.getAmountBoilerPrint(), "", now);
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public BoilerOrderOperationResponse updateOrder(@Valid Canban canban) {
        try {
            var boilerType = boilerTypeCycleRepository.findByArticleAndIsActive(canban.article(), true).
                    orElseThrow(() -> new BoilerTypeNotFoundException("Тип котла не найден"));
            BoilerOrder boilerOrder = getBoilerOrder(canban.id());
            boilerOrderHelperService.checkOrder(boilerOrder);
            boilerOrderHelperService.updateOrder(canban, boilerOrder, boilerType);
            partLastRepository.updatePart_idByStation(boilerOrder.getId(), "wp1");
            return new BoilerOrderOperationResponse(boilerOrder.getId(), true,
                    boilerOrder.getOrderNumber(), boilerType.getArticle(), boilerOrder.getAmountBoilerOrder(),
                    boilerOrder.getAmountBoilerPrint(), boilerOrder.getScanCode(), boilerOrder.getModifiedDate());
        } catch (BoilerTypeNotFoundException | BoilerOrderReadyNotFoundException e) {
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    public BoilerOrder setAmountBoilerPrint(BoilerOrder boilerOrder, Integer amountBarcodes) {
        boilerOrder.setAmountBoilerPrint(boilerOrder.getAmountBoilerPrint() + amountBarcodes);
        return boilerOrderRepository.save(boilerOrder);
    }
}
