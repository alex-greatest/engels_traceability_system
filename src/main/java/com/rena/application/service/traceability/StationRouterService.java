package com.rena.application.service.traceability;

import com.rena.application.repository.traceability.common.StationRouterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StationRouterService {
    private final StationRouterRepository stationRouterRepository;


}
