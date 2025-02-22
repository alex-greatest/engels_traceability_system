package com.rena.application.service.result;

import com.rena.application.repository.result.common.BoilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoilerService {
    private final BoilerRepository boilerRepository;

    public void getAllBoilers() {
        boilerRepository.findAll();
    }
}
