package com.rena.application.controller.traceability.websocket.common;

import com.rena.application.entity.dto.settings.user.station.OperatorRequestAuthorization;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.exchange.StationNameData;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.service.traceability.common.user.UserTraceabilityService;
import com.rena.application.service.traceability.helper.ErrorHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserAuthorization {
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorHelper errorHelper;
    private final UserTraceabilityService userTraceabilityService;

    @MessageMapping("/operator/authorization/request")
    public void getOperatorAuthorization(@Payload OperatorRequestAuthorization operatorRequestAuthorization) {
        try {
            var operator = userTraceabilityService.getOperatorAuthorization(operatorRequestAuthorization);
            operator.setCorrelationId(operatorRequestAuthorization.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response",
                    operatorRequestAuthorization.getStation()), operator);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operatorRequestAuthorization.getCorrelationId());
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operatorRequestAuthorization.getCorrelationId());
            log.error("Получение данных оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        }
    }

    @MessageMapping("/operator/authorization/logout/request")
    public void stationOperatorLogOut(@Payload OperatorRequestAuthorization operatorRequestAuthorization) {
        try {
            userTraceabilityService.stationOperatorLogout(operatorRequestAuthorization.getLogin());
            var rpcBase = new RpcBase();
            rpcBase.setCorrelationId(operatorRequestAuthorization.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/response",
                    operatorRequestAuthorization.getStation()), rpcBase);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operatorRequestAuthorization.getCorrelationId());
            log.error("Выход из аккаунта оператора", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/response/error",
                            operatorRequestAuthorization.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка",
                    operatorRequestAuthorization.getCorrelationId());
            log.error("Выход из аккаунта оператор", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/operator/authorization/logout/response/error",
                            operatorRequestAuthorization.getStation()), error);
        }
    }

    @MessageMapping("/admin/authorization/request")
    public void getAdminAuthorization(@Payload OperatorRequestAuthorization operatorRequestAuthorization) {
        try {
            var user = userTraceabilityService.getAdminAuthorization(operatorRequestAuthorization);
            user.setCorrelationId(operatorRequestAuthorization.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/response",
                    operatorRequestAuthorization.getStation()), user);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operatorRequestAuthorization.getCorrelationId());
            log.error("Авторизация админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operatorRequestAuthorization.getCorrelationId());
            log.error("Авторизация админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/response/error",
                            operatorRequestAuthorization.getStation()), error);
        }
    }

    @MessageMapping("/admin/authorization/logout/request")
    public void stationAdminLogOut(@Payload OperatorRequestAuthorization operatorRequestAuthorization) {
        try {
            userTraceabilityService.stationAdminLogout(operatorRequestAuthorization.getLogin());
            var rpcBase = new RpcBase();
            rpcBase.setCorrelationId(operatorRequestAuthorization.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/logout/response",
                    operatorRequestAuthorization.getStation()), rpcBase);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), operatorRequestAuthorization.getCorrelationId());
            log.error("Выход из аккаунта админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/logout/response/error",
                            operatorRequestAuthorization.getStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", operatorRequestAuthorization.getCorrelationId());
            log.error("Выход из аккаунта админа", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/admin/authorization/logout/response/error",
                            operatorRequestAuthorization.getStation()), error);
        }
    }

    @MessageMapping("/users/last/login/request")
    public void getLastLoginUser(@Payload @Valid StationNameData stationNameData) {
        try {
            var userLoginLast = userTraceabilityService.getUserLastLogin(stationNameData.getNameStation());
            userLoginLast.setCorrelationId(stationNameData.getCorrelationId());
            messagingTemplate.convertAndSend(String.format("/message/%s/users/last/login/response",
                    stationNameData.getNameStation()), userLoginLast);
        } catch (RecordNotFoundException e) {
            var error = errorHelper.getErrorResponse(e.getMessage(), stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции. Получение инфорамации о залогининых пользователях", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/users/last/login/response/error",
                    stationNameData.getNameStation()), error);
        } catch (Exception e) {
            var error = errorHelper.getErrorResponse("Неизвестная ошибка", stationNameData.getCorrelationId());
            log.error("Иниицалазиция станции. Получение инфорамации о залогининых пользователях", e);
            messagingTemplate.convertAndSend(String.format("/message/%s/users/last/login/response/error",
                    stationNameData.getNameStation()), error);
        }
    }
}