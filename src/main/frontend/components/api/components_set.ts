import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { ComponentSetController } from 'Frontend/generated/endpoints';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import ComponentSetList from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentSetList';
import ComponentSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentSetDto';
import {
  errorMessageEmpty,
  errorMessageLength50,
  validateLength,
  validateRequired
} from 'Frontend/components/api/helper';

export function useComponentSet(id: number, url: string) {
  return useQuery({
    queryKey: ['components_set', url],
    queryFn: () => ComponentSetController.getAllComponentSet(id),
    staleTime: 0,
    enabled: id !== undefined && !isNaN(id)
  })
}

export const componentSetAddMutation = (queryClient: QueryClient, url: string) => useMutation({
  mutationFn: ({ componentNameSetId, componentSetDto }: { componentNameSetId: number; componentSetDto: ComponentSetDto }) =>
    ComponentSetController.addComponentSet(componentNameSetId, componentSetDto),
  onMutate: async (newComponentSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_set', url] });
    const previous = queryClient.getQueryData<ComponentSetList>(['components_set', url]);
    queryClient.setQueryData<ComponentSetList>(['components_set', url],
        old => {
            console.log(old?.componentsSet);
            if (!old) return old;
              return {
                ...old,
                componentsSet: [...(old.componentsSet || []), newComponentSet.componentSetDto],
              };
        });
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_set_add_success", "Компонент успешно добавлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentSetList>(['components_set', url], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_set_add_error", error.message);
    } else {
      showErrorMessage("components_set_add_error", "Неизвестная ошибка");
    }
  }
});

export const componentSetEditMutation = (queryClient: QueryClient, url: string) => useMutation({
  mutationFn:  ComponentSetController.updateComponentSet,
  onSuccess: () => {
    showSuccessMessage("components_set_edit_success", "Компонент(ы) обновлен(ы)");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_set_edit_error", error.message);
    } else {
      showErrorMessage("components_set_edit_error", "Неизвестная ошибка");
    }
  },
})

export const componentSetDelete = (queryClient: QueryClient, url: string) => useMutation({
  mutationFn: ComponentSetController.deleteComponent,
  onMutate: async (newComponentSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_set', url] });
    const previous = queryClient.getQueryData<ComponentSetList>(['components_set', url]);
    queryClient.setQueryData<ComponentSetList>(['components_set', url], old => {
      console.log(url);
      if (!old) return old;
      return {
        ...old,
        componentsSet: old.componentsSet.filter((t) => t.id !== newComponentSet)
      };
    });
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_set_delete_success", "Компонент успешно удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<ComponentSetList>(['components_set', url], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_set_delete_error", error.message);
    } else {
      showErrorMessage("components_set_delete_error", "Неизвестная ошибка");
    }
  }
});

export const componentSetCopyAllComponentsSetMutation = (queryClient: QueryClient, url: string) => useMutation({
  mutationFn:  ComponentSetController.copyAllComponentsType,
  onSuccess: () => {
    showSuccessMessage("components_set_copy_values_success", "Данные успешно скопированы");
  },
  onError: (error, _, context) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_set_copy_values_error", error.message);
    } else {
      showErrorMessage("components_set_copy_values_error", "Неизвестная ошибка");
    }
  },
})

export function validateComponentSet(componentSetDto: ComponentSetDto) {
  return {
    'mrt-row-create_componentType.name': !validateRequired(componentSetDto.componentType?.name ?? "")
      ? errorMessageEmpty
      : undefined,
    'mrt-row-create_value': !validateLength(componentSetDto.value ?? "") ? errorMessageLength50 : undefined,
  };
}