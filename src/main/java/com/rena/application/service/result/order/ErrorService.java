package com.rena.application.service.result.order;

import com.rena.application.entity.model.result.error.Error;
import com.rena.application.repository.result.error.ErrorRepository;
import com.rena.application.repository.result.error.ErrorTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ErrorService {
    private final ErrorRepository errorRepository;
    private final ErrorTemplateRepository errorTemplateRepository;

    @Transactional
    public void addError(String message, String station, Long userId, Long boilerId) {
        errorTemplateRepository.findByMessageAndStation_Name(message, station).ifPresent((errorTemplate) -> {
            var error = new Error();
            error.setErrorTemplate(errorTemplate);
            error.setBoilerId(boilerId);
            error.setId(userId);
            error.setDateCreate(LocalDateTime.now());
            errorRepository.save(error);
        });
    }
}
