import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { ComponentNameSetController } from 'Frontend/generated/endpoints';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';

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
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_name_set_add_error", error.message);
    } else {
      showErrorMessage("components_name_set_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["components_name_set"] }).then();
  }
});

export const componentNameSetEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ({ id, oldName, component }: { id: number; oldName: string; component: ComponentNameSetDto }) =>
    ComponentNameSetController.updateComponent(id, oldName, component),
  onMutate: async (newComponentNameSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_name_set'] });
    const previous = queryClient.getQueryData<ComponentNameSetDto[]>(['components_name_set']);
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], old => [...(old as ComponentNameSetDto[]),
      (newComponentNameSet.component as ComponentNameSetDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_edit_success", "Набор компонентов обновлен");
  },
  onError: (error, _, context) => {
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
  onMutate: async (newComponentNameSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_name_set'] });
    const previous = queryClient.getQueryData<ComponentNameSetDto[]>(['components_name_set']);
    queryClient.setQueryData<ComponentNameSetDto[]>(['components_name_set'], old => old?.filter((t) => t.id !== newComponentNameSet));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_delete_success", "Набор компонентов успешно удалён");
  },
  onError: (error, _, context) => {
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

const validateRequired = (value: string) => !!value.trim().length;

export function validateComponentNameSet(componentNameSetDto: ComponentNameSetDto) {
  return {
    name: !validateRequired(componentNameSetDto?.name ?? "")
      ? 'Название не может быть пустым'
      : '',
  };
}
