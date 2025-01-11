import { useQuery } from '@tanstack/react-query';
import { RoleService, UserService } from 'Frontend/generated/endpoints';

export function useRoles() {
  return useQuery({
    queryKey: ['roles'],
    queryFn: RoleService.getAllRoles,
    staleTime: Infinity
  })
}