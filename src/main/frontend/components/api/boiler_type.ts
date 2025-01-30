import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { BoilerController } from 'Frontend/generated/endpoints';
import BoilerTypeDto from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeDto';
import {
  errorMessageEmpty,
  errorMessageLength50,
  validateLength,
  validateRequired
} from 'Frontend/components/api/helper';

export function useBoilers() {
  return useQuery({
    queryKey: ['boiler'],
    queryFn: BoilerController.getAllBoiler,
    staleTime: 1000 * 60 * 5
  })
}

export const boilerTypeAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.addBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerTypeDto[]>(['boiler']);
    queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], old => [...(old as BoilerTypeDto[]), (newBoiler as BoilerTypeDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_add_success", "Тип котла успешно добавлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], context.previous)
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

export const boilerTypeEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.updateBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerTypeDto[]>(['boiler']);
    queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], old => [...(old as BoilerTypeDto[]), (newBoiler as BoilerTypeDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_edit_success", "Тип котла обновлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], context.previous)
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

export const boilerTypeDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerController.deleteBoiler,
  onMutate: async (newBoiler) => {
    await queryClient.cancelQueries({ queryKey: ['boiler'] });
    const previous = queryClient.getQueryData<BoilerTypeDto[]>(['boiler']);
    queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], old => old?.filter((t) => t.id !== newBoiler));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("components_name_set_delete_success", "Тип котла успешно удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<BoilerTypeDto[]>(['boiler'], context?.previous);
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

export function validateBoilerType(boiler: BoilerTypeDto) {
  return {
    typeName: !validateLength(boiler?.typeName ?? "")
      ? errorMessageLength50
      : '',
    model: !validateLength(boiler?.model ?? "")
      ? errorMessageLength50
      : '',
    componentNameSet: !validateRequired(boiler?.componentNameSet.name ?? "")
      ? errorMessageEmpty
      : '',
  };
}
