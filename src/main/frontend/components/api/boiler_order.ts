import {useQuery } from '@tanstack/react-query';
import { BoilerOrderResultController, BoilerTypeController } from 'Frontend/generated/endpoints';

export function useBoilers() {
  return useQuery({
    queryKey: ['boiler'],
    queryFn: BoilerTypeController.getAllBoiler,
    staleTime: 1000 * 60 * 5,
    enabled: false, // Disable the query by default
  });
}

export function useBoilerOrdersByDateRange(startDate: string, endDate: string) {
  return useQuery({
    queryKey: ['boilerOrder', startDate, endDate],
    queryFn: () => BoilerOrderResultController.getBoilerOrdersByDateRange(startDate, endDate),
    staleTime: 1000 * 60 * 5,
    enabled: !!startDate && !!endDate,
  });
}