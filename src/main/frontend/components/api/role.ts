import { useQuery } from '@tanstack/react-query';
import { RoleController } from 'Frontend/generated/endpoints';

export function useRoles() {
  return useQuery({
    queryKey: ['roles'],
    queryFn: RoleController.getAllRoles,
    staleTime: Infinity
  })
}