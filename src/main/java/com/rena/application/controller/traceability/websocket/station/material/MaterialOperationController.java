package com.rena.application.controller.traceability.websocket.station.material;

import com.rena.application.entity.dto.traceability.station.material.operation.MaterialsOperationSaveResultRequest;
import com.rena.application.entity.dto.traceability.station.material.result.MaterialResultRequest;
import com.rena.application.entity.dto.traceability.station.material.result.MaterialsResult;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.helper.ErrorHelper;
import com.rena.application.service.traceability.station.material.MaterialResultSaveService;
import com.rena.application.service.traceability.station.material.MaterialsResultSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MaterialOperationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MaterialsResultSaveService materialsResultSaveService;
    private final MaterialResultSaveService materialResultSaveService;
    private final ErrorHelper errorHelper;

    @MessageMapping("/materials/material/save/request")
    public void saveMaterial(@Payload MaterialResultRequest materialsResult) {
        try {
            var materialResultResponse = materialResultSaveService.saveResultsMaterial(materialsResult);
            materialResultResponse.setCorrelationId(materialsResult.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/material/save/response",
                    materialsResult.getStationName()), materialResultResponse);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), materialsResult.getCorrelationId());
            log.error("Привязка материала. Станция {}", materialsResult.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/material/save/response/error",
                    materialsResult.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", materialsResult.getCorrelationId());
            log.error("Привязка материала. Станция {}", materialsResult.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/component/save/response/error",
                    materialsResult.getStationName()), error);
        }
    }

    @MessageMapping("/materials/result/operation/save/request")
    public void saveMaterialsResult(@Payload MaterialsOperationSaveResultRequest materialsOperationSaveResultRequest) {
        try {
            var boilerMadeInformation = materialsResultSaveService.saveResultsMaterial(materialsOperationSaveResultRequest);
            boilerMadeInformation.setCorrelationId(materialsOperationSaveResultRequest.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/station/%s/materials/save/operation/response",
                    materialsOperationSaveResultRequest.getStationName()), boilerMadeInformation);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), materialsOperationSaveResultRequest.getCorrelationId());
            log.error("Сохрание материалов. Станция {}", materialsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/materials/save/operation/response/error",
                    materialsOperationSaveResultRequest.getStationName()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", materialsOperationSaveResultRequest.getCorrelationId());
            log.error("Сохрание материалов. Станция {}", materialsOperationSaveResultRequest.getStationName(), e);
            messagingTemplate.convertAndSend(String.format("/message/station/%s/materials/save/operation/response/error",
                    materialsOperationSaveResultRequest.getStationName()), error);
        }
    }
}

