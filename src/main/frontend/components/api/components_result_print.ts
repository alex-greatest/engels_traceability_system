import { useQuery } from '@tanstack/react-query';
import { ComponentResultController } from 'Frontend/generated/endpoints';
import ComponentSet from 'Frontend/generated/com/rena/application/entity/model/component/set/ComponentSet';

/**
 * Hook для получения компонентов по ID операции
 * @param operationId - ID операции для поиска компонентов
 */
export function useComponentsByOperationId(operationId: number) {
  return useQuery<ComponentSet[]>({
    queryKey: ['components', 'operation', operationId],
    queryFn: () => ComponentResultController.getComponentsByOperationId(operationId),
    staleTime: 1000 * 60 * 5, // 5 минут
    enabled: !!operationId, // Запрос выполняется только если ID операции указан
  });
} 