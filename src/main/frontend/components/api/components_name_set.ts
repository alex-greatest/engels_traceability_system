import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { ComponentNameSetController } from 'Frontend/generated/endpoints';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import ComponentDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentDto';

export function useComponentsNameSet() {
  return useQuery({
    queryKey: ['components_name_set'],
    queryFn: ComponentNameSetController.getAllComponents,
    staleTime: 1000 * 60 * 5
  })
}

export const componentNameSetAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentNameSetController.addComponent,
  onMutate: async (newComponentNameSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_name_set'] });
    const previous = queryClient.getQueryData<ComponentNameSetDto[]>(['components_name_set']);
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], old => [...(old as ComponentNameSetDto[]), (newComponentNameSet as ComponentNameSetDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_add_success", "Набор компонентов успешно добавлен");
  },
  onError: (error, newComponentNameSet, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_name_set_add_error", error.message);
    } else {
      console.log(error);
      showErrorMessage("components_name_set_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_name_set"] }).then();
  }
});

export const componentNameSetEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ({ id, oldName, component }: { id: number; oldName: string; component: ComponentDto }) =>
    ComponentNameSetController.updateComponent(id, oldName, component),
  onMutate: async (newComponent) => {
    await queryClient.cancelQueries({ queryKey: ['components_name_set'] });
    const previous = queryClient.getQueryData<ComponentNameSetDto[]>(['components_name_set']);
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], old => [...(old as ComponentNameSetDto[]),
      (newComponent as ComponentNameSetDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_edit_success", "Набор компонентов обновлен");
  },
  onError: (error, newComponentNameSet, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_name_set_edit_error", error.message);
    } else {
      showErrorMessage("components_name_set_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_name_set"] }).then();
  }
})

export const componentNameSetDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: ComponentNameSetController.deleteComponent,
  onMutate: async (newTodo) => {
    await queryClient.cancelQueries({ queryKey: ['components_name_set'] });
    const previous = queryClient.getQueryData<ComponentNameSetDto[]>(['components_name_set']);
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], old => old?.filter((t) => t.id !== newTodo));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_delete_success", "Набор компонентов успешно удалён");
  },
  onError: (error, newComponentNameSet, context) => {
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_name_set_delete_error", error.message);
    } else {
      showErrorMessage("components_name_set_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_name_set"] }).then();
  }
});

const validateRequired = (value: string) => !!value.length;

export function validateComponentNameSet(component: ComponentDto) {
  return {
    name: !validateRequired(component?.name ?? "")
      ? 'Название не может быть пустым'
      : '',
  };
}
