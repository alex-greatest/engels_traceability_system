import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import ComponentDto from 'Frontend/generated/com/rena/application/entity/dto/user/ComponentDto';
import { ComponentController } from 'Frontend/generated/endpoints';

export function useComponents() {
  return useQuery({
    queryKey: ['components'],
    queryFn: ComponentController.getAllComponents,
    staleTime: 1000 * 60 * 5
  })
}

export const componentAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentController.addComponent,
  onMutate: async (newComponent) => {
    await queryClient.cancelQueries({ queryKey: ['components'] });
    const previous = queryClient.getQueryData<ComponentDto[]>(['components']);
    queryClient.setQueryData<ComponentDto[]>(['components'], old => [...(old as ComponentDto[]), (newComponent as ComponentDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_add_success", "Компонент успешно добавлен");
  },
  onError: (error, newComponent, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentDto[]>(['components'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_add_error", error.message);
    } else {
      showErrorMessage("components_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components"] }).then();
  }
});

export const componentEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ({ id, oldName, component }: { id: number; oldName: string; component: ComponentDto }) =>
    ComponentController.updateComponent(id, oldName, component),
  onMutate: async (newComponent) => {
    await queryClient.cancelQueries({ queryKey: ['components'] });
    const previous = queryClient.getQueryData<ComponentDto[]>(['components']);
    queryClient.setQueryData<ComponentDto[]>(['components'], old => [...(old as ComponentDto[]), (newComponent as ComponentDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_edit_success", "Компонент обновлен");
  },
  onError: (error, newComponent, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentDto[]>(['components'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_edit_error", error.message);
    } else {
      showErrorMessage("components_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components"] }).then();
  }
})

export const componentDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentController.deleteComponent,
  onMutate: async (newTodo) => {
    await queryClient.cancelQueries({ queryKey: ['components'] });
    const previous = queryClient.getQueryData<ComponentDto[]>(['components']);
    queryClient.setQueryData<ComponentDto[]>(['components'], old => old?.filter((t) => t.id !== newTodo));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_delete_success", "Компонент успешно удалён");
  },
  onError: (error, newComponent, context) => {
    queryClient.setQueryData<ComponentDto[]>(['components'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_delete_error", error.message);
    } else {
      showErrorMessage("components_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components"] }).then();
  }
});

const validateRequired = (value: string) => !!value.length;

export function validateComponent(component: ComponentDto) {
  return {
    name: !validateRequired(component?.name ?? "")
      ? 'Название компонента не может быть пустым'
      : '',
  };
}
