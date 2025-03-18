package com.rena.application.service.result.print;

import com.rena.application.entity.model.component.set.ComponentSet;
import com.rena.application.repository.component.set.ComponentSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ComponentResultService {
    private final ComponentSetRepository componentSetRepository;

    public ComponentResultService(ComponentSetRepository componentSetRepository) {
        this.componentSetRepository = componentSetRepository;
    }

    /**
     * Получает список компонентов по ID операции
     * @param operationId ID операции
     * @return список компонентов
     */
    public List<ComponentSet> getComponentsByOperationId(Long operationId) {
        return componentSetRepository.findAllComponentsById(operationId);
    }
} 