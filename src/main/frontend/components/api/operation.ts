import { useQuery } from '@tanstack/react-query';
import { OperationControllerResult } from 'Frontend/generated/endpoints';
import OperationResult from 'Frontend/generated/com/rena/application/entity/dto/result/print/OperationResult';

/**
 * Hook для получения операций по серийному номеру
 * @param serialNumber - серийный номер для поиска
 */
export function useOperationsBySerial(serialNumber: string) {
  return useQuery<OperationResult[]>({
    queryKey: ['operations', 'serial', serialNumber],
    queryFn: () => OperationControllerResult.getOperationsBySerial(serialNumber),
    staleTime: 1000 * 60 * 5, // 5 минут
    enabled: !!serialNumber, // Запрос выполняется только если серийный номер указан
  });
}
