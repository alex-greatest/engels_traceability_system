package com.rena.application.service;

import com.rena.application.exceptions.RecordNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Helper {
    public String extractArticleNumber(String articleNumber) {
        if (articleNumber == null || articleNumber.length() <= 10) {
            throw new RecordNotFoundException("Код слишком короткий");
        }
        return articleNumber.substring(articleNumber.length() - 10);
    }
}
