package com.rena.application.service.result;

import com.rena.application.entity.model.result.error.Error;
import com.rena.application.entity.model.user.UserHistory;
import com.rena.application.repository.result.error.ErrorRepository;
import com.rena.application.repository.result.error.ErrorTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ErrorService {
    private final ErrorRepository errorRepository;
    private final ErrorTemplateRepository errorTemplateRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addError(String message, String station, String step, UserHistory userHistory, String serialNumber) {
        errorTemplateRepository.findByStation_NameAndErrorMessage_Name(station, message, step).ifPresent((errorTemplate) -> {
            var error = new Error();
            error.setErrorTemplate(errorTemplate);
            error.setSerialNumber(serialNumber);
            error.setUserHistory(userHistory);
            error.setDateCreate(LocalDateTime.now());
            errorRepository.save(error);
        });
    }
}
