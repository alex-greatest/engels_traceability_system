import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import {
  ComponentBindingController,
  ComponentTypeController
} from 'Frontend/generated/endpoints';
import {
  errorMessageEmpty,
  validateRequired
} from 'Frontend/components/api/helper';
import ComponentBindingResponse from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentBindingResponse';
import ComponentBindingRequest
  from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentBindingRequest';

export function useComponentsBinding(idNameSet: number, nameStation: string) {
  return useQuery({
    queryKey: ['components_binding', nameStation, idNameSet],
    queryFn: () => ComponentBindingController.getAllBindingComponents(nameStation, idNameSet),
    staleTime: 0,
    enabled: idNameSet !== undefined && !isNaN(idNameSet)
  })
}

export function useComponentsTypeSet(idNameSet: number, nameStation: string) {
  return useQuery({
    queryKey: ['component_type_set', nameStation],
    queryFn: () => ComponentTypeController.getAllComponentsTypeSet(idNameSet),
    staleTime: 0,
    enabled: !isNaN(idNameSet) && Boolean(nameStation)
  })
}

export const componentBindingAddMutation = (queryClient: QueryClient,
                                            idNameSet: number,
                                            nameStation: string) => useMutation({
  mutationFn: ComponentBindingController.addComponentBinding,
  onMutate: async (newComponentSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_binding', nameStation, idNameSet] });
    const previous = queryClient.getQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet]);
    queryClient.setQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet],
      old => {
        if (!old) return old;
        return {
          ...old,
        componentBindingResponse: {id: newComponentSet.id, componentType: newComponentSet.componentType, order: newComponentSet.order},
        };
      });
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_binding_add_success", "Привязка успешно добавлена");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_binding_add_error", error.message);
    } else {
      showErrorMessage("components_binding_add_error", "Неизвестная ошибка");
    }
  }
});

export const componentBindingEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn:  ComponentBindingController.updateComponentBinding,
  onSuccess: () => {
    showSuccessMessage("components_binding_edit_success", "Привязка(ы) обновлен(ы)");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_binding_edit_error", error.message);
    } else {
      showErrorMessage("components_binding_edit_error", "Неизвестная ошибка");
    }
  },
})

export const componentBindingDelete = (
                                    queryClient: QueryClient,
                                    idNameSet: number,
                                    nameStation: string) => useMutation({
  mutationFn: ComponentBindingController.deleteComponentBinding,
  onMutate: async (newComponentSet) => {
    await queryClient.cancelQueries({ queryKey: ['components_binding', nameStation, idNameSet] });
    const previous = queryClient.getQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet]);
    queryClient.setQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet], old => {
      if (!old) return old;
      return {
        ...old,
        componentsSet: old.filter((t) => t.id !== newComponentSet)
      };
    });
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_binding_delete_success", "Компонент успешно удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<ComponentBindingResponse[]>(['components_binding', nameStation, idNameSet], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_binding_delete_error", error.message);
    } else {
      showErrorMessage("components_binding_delete_error", "Неизвестная ошибка");
    }
  }
});

export function validateComponentBinding(componentBinding: ComponentBindingRequest) {
  return {
    'mrt-row-create_componentType.name': !validateRequired(componentBinding.componentType?.name ?? "")
      ? errorMessageEmpty
      : undefined,
    order: (componentBinding.order ?? 0) <= 0 || !componentBinding.order
      ? "Порядок должен быть больше 0"
      : undefined,
  };
}
