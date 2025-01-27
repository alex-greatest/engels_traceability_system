package com.rena.application.service.boiler;

import com.rena.application.config.mapper.BoilerMapper;
import com.rena.application.entity.dto.boiler.BoilerDto;
import com.rena.application.entity.model.boiler.Boiler;
import com.rena.application.entity.model.component.ComponentNameSet;
import com.rena.application.exceptions.RecordNotFoundException;
import com.rena.application.repository.boiler.BoilerRepository;
import com.rena.application.repository.component.ComponentNameSetRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class BoilerService {
    private final BoilerRepository boilerRepository;
    private final BoilerMapper boilerMapper;
    private final BoilerHistoryService boilerHistoryService;
    private final ComponentNameSetRepository componentNameSetRepository;

    public List<BoilerDto> getAllBoilers() {
        List<Boiler> boilers = boilerRepository.findAll();
        return boilerMapper.toBoilerDto(boilers);
    }

    @Transactional
    public void addBoiler(BoilerDto boilerDto) {
        Boiler boiler = boilerMapper.toEntity(boilerDto);
        boilerRepository.save(boiler);
        boilerHistoryService.addBoilerHistory(boiler.getId(), null, boiler.getComponentNameSet().getName(),
                boiler.getArticle(), boiler.getName(), true, 1);
    }

    @Transactional
    public void updateBoiler(BoilerDto boilerDto) {
        Boiler boiler = boilerRepository.findBoilerById(boilerDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        ComponentNameSet componentNameSetNew = componentNameSetRepository.findById(boilerDto.id()).
                orElseThrow(() -> new RecordNotFoundException("Набора компонентов не найден"));
        String oldArticle = boiler.getArticle();
        boilerHistoryService.addBoilerHistory(boiler.getId(), oldArticle, componentNameSetNew.getName(),
                boilerDto.article(), boilerDto.name(), true, 2);
        boiler.setComponentNameSet(componentNameSetNew);
        boiler.setArticle(boilerDto.article());
        boiler.setName(boilerDto.name());
        boilerRepository.save(boiler);
    }

    @Transactional
    public void deleteComponent(Long id) {
        Boiler boiler = boilerRepository.findBoilerById(id).
                orElseThrow(() -> new RecordNotFoundException("Котёл не найден"));
        boilerHistoryService.addBoilerHistory(boiler.getId(), null,
                boiler.getComponentNameSet().getName(), boiler.getArticle(), boiler.getName(), true, 3);
        boilerRepository.delete(boiler);
    }
}
