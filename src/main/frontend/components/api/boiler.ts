import { useQuery } from '@tanstack/react-query';
import { BoilerResultController } from 'Frontend/generated/endpoints';

/**
 * Hook для получения всех котлов
 */
export function useAllBoilers() {
  return useQuery({
    queryKey: ['allBoilers'],
    queryFn: BoilerResultController.getAllBoilers,
    staleTime: 1000 * 60 * 5,
  });
}

/**
 * Hook для получения котлов по диапазону дат
 * @param startDate - начальная дата в формате ISO
 * @param endDate - конечная дата в формате ISO
 */
export function useBoilersByDateRange(startDate: string, endDate: string) {
  return useQuery({
    queryKey: ['boilers', 'dateRange', startDate, endDate],
    queryFn: () => BoilerResultController.getBoilersByDateRange(startDate, endDate),
    staleTime: 1000 * 60 * 5, // 5 минут
    enabled: !!startDate && !!endDate, // Запрос выполняется только если обе даты указаны
  });
}

/**
 * Hook для получения котлов по серийному номеру
 * @param serialNumber - серийный номер для поиска
 */
/*export function useBoilersBySerialNumber(serialNumber: string) {
  return useQuery({
    queryKey: ['boilers', 'serialNumber', serialNumber],
    queryFn: () => BoilerResultController.getBoilerBySerialNumber(serialNumber),
    staleTime: 1000 * 60 * 5, // 5 минут
    enabled: !!serialNumber, // Запрос выполняется только если серийный номер указан
  });
} */