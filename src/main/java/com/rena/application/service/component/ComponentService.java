package com.rena.application.service.component;

import com.rena.application.entity.model.component.set.ComponentSet;
import com.rena.application.repository.component.set.ComponentSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentSetRepository componentSetRepository;

    /**
     * Получает список компонентов по ID операции
     * @param operationId ID операции
     * @return список компонентов
     */
    public List<ComponentSet> getComponentsByOperationId(Long operationId) {
        return componentSetRepository.findAllComponentsById(operationId);
    }
} 