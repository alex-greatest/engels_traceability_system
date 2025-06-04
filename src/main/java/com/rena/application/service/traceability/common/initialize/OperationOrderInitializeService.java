package com.rena.application.service.traceability.common.initialize;

import com.rena.application.entity.dto.traceability.station.order.BoilerOrderOperationResponse;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.result.BoilerOrderRepository;
import com.rena.application.repository.settings.PartLastRepository;
import com.rena.application.service.traceability.station.order.BoilerOrderHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OperationOrderInitializeService {
    private final PartLastRepository partLastRepository;
    private final BoilerOrderRepository boilerOrderRepository;
    private final BoilerOrderHelperService boilerOrderHelperService;

    @Transactional
    public BoilerOrderOperationResponse getLastBoilerOrder(String nameStation) {
        var idOpt =  partLastRepository.findByStation_Name(nameStation);
        if (idOpt.isPresent() && idOpt.get().getPart_id() != null && !idOpt.get().getPart_id().trim().isEmpty()) {
            var id = idOpt.get();
            var boilerOrder = boilerOrderRepository.findById(id.getPart_id()).
                    orElseThrow(() -> new RecordNotFoundException("Последний заказ не найден"));
            boilerOrderHelperService.checkOrder(boilerOrder, nameStation);
            return new BoilerOrderOperationResponse(boilerOrder.getId(), boilerOrder.getOrderNumber(),
                    boilerOrder.getBoilerTypeCycle().getArticle(),
                    boilerOrder.getAmountBoilerOrder(), boilerOrder.getAmountBoilerPrint(),
                    boilerOrder.getMainCode(), boilerOrder.getCanbanCode());
        }
        return new BoilerOrderOperationResponse("", 0,
                "", 0, 0, "", "");
    }
}
