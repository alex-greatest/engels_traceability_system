import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { ComponentTypeController } from 'Frontend/generated/endpoints';
import ComponentTypeDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentTypeDto';

export function useComponents() {
  return useQuery({
    queryKey: ['components_type'],
    queryFn: ComponentTypeController.getAllComponents,
    staleTime: 1000 * 60 * 5
  })
}

export const componentAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentTypeController.addComponent,
  onMutate: async (newComponentType) => {
    await queryClient.cancelQueries({ queryKey: ['components_type'] });
    const previous = queryClient.getQueryData<ComponentTypeDto[]>(['components_type']);
    queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], old => [...(old as ComponentTypeDto[]), (newComponentType as ComponentTypeDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_type_add_success", "Тип компонента успешно добавлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_type_add_error", error.message);
    } else {
      showErrorMessage("components_type_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_type"] }).then();
  }
});

export const componentEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ({ id, component }: { id: number; component: ComponentTypeDto }) =>
    ComponentTypeController.updateComponent(id, component),
  onMutate: async (newComponentType) => {
    await queryClient.cancelQueries({ queryKey: ['components_type'] });
    const previous = queryClient.getQueryData<ComponentTypeDto[]>(['components_type']);
    queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], old => [...(old as ComponentTypeDto[]),
      (newComponentType.component as ComponentTypeDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_type_edit_success", "Тип компонента обновлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_type_edit_error", error.message);
    } else {
      showErrorMessage("components_type_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_type"] }).then();
  }
})

export const componentDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentTypeController.deleteComponent,
  onMutate: async (newComponentType) => {
    await queryClient.cancelQueries({ queryKey: ['components_type'] });
    const previous = queryClient.getQueryData<ComponentTypeDto[]>(['components_type']);
    queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], old => old?.filter((t) => t.id !== newComponentType));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_type_delete_success", "Тип компонента успешно удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<ComponentTypeDto[]>(['components_type'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_type_delete_error", error.message);
    } else {
      showErrorMessage("components_type_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_type"] }).then();
  }
});

const validateRequired = (value: string) => !!value.trim().length;

export function validateComponent(componentType: ComponentTypeDto) {
  return {
    name: !validateRequired(componentType?.name ?? "")
      ? 'Название компонента не может быть пустым'
      : '',
  };
}
