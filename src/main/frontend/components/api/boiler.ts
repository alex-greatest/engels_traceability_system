import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { BoilerController, ComponentNameSetController } from 'Frontend/generated/endpoints';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import BoilerDto from 'Frontend/generated/com/rena/application/entity/dto/boiler/BoilerDto';

export function useBoilers() {
  return useQuery({
    queryKey: ['boiler'],
    queryFn: BoilerController.getAllBoiler,
    staleTime: 1000 * 60 * 5
  })
}

export const boilerAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.addBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerDto[]>(['boiler']);
    queryClient.setQueryData<BoilerDto[]>(['boiler'], old => [...(old as BoilerDto[]), (newBoiler as BoilerDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_add_success", "Котёл успешно добавлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerDto[]>(['boiler'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_add_error", error.message);
    } else {
      showErrorMessage("boiler_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ['boiler'] }).then();
  }
});

export const boilerEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.updateBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerDto[]>(['boiler']);
    queryClient.setQueryData<BoilerDto[]>(['boiler'], old => [...(old as BoilerDto[]), (newBoiler as BoilerDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_edit_success", "Котёл обновлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerDto[]>(['boiler'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_edit_error", error.message);
    } else {
      showErrorMessage("boiler_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ['boiler'] }).then();
  }
})

export const boilerDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.deleteBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerDto[]>(['boiler']);
    queryClient.setQueryData<BoilerDto[]>(['boiler'], old => old?.filter((t) => t.id !== newBoiler));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_delete_success", "Котёл успешно удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<BoilerDto[]>(['boiler'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("components_name_set_delete_error", error.message);
    } else {
      showErrorMessage("components_name_set_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ['boiler']}).then();
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
