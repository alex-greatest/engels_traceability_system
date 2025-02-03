import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { BoilerTypeAdditionalSetController } from 'Frontend/generated/endpoints';
import {
  errorMessageLength100,
  validateLength100,
} from 'Frontend/components/api/helper';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';

export function useBoilerDataSet() {
  return useQuery({
    queryKey: ['boiler_data_set'],
    queryFn: BoilerTypeAdditionalSetController.getAllDataSet,
    staleTime: 1000 * 60 * 5
  })
}

export const boilerDataSetAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerTypeAdditionalSetController.addDataSet,
  onMutate: async (boilerTypeAdditionalSetNew) => {
    await queryClient.cancelQueries({ queryKey: ['boiler_data_set'] });
    const previous = queryClient.getQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set']);
    queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], old => [...(old as BoilerTypeAdditionalDataSetDto[]),
      (boilerTypeAdditionalSetNew as BoilerTypeAdditionalDataSetDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_data_set_add_success", "Набор данных котла успешно добавлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_data_set_add_error", error.message);
    } else {
      showErrorMessage("boiler_data_set_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["boiler_data_set"] }).then();
  }
});

export const boilerDataSetEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerTypeAdditionalSetController.updateDataSet,
  onMutate: async (boilerTypeAdditionalSetNew) => {
    await queryClient.cancelQueries({ queryKey: ['boiler_data_set'] });
    const previous = queryClient.getQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set']);
    queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], old => [...(old as BoilerTypeAdditionalDataSetDto[]),
      (boilerTypeAdditionalSetNew as BoilerTypeAdditionalDataSetDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_data_set_edit_success", "Набор данных котла обновлен");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_data_set_edit_error", error.message);
    } else {
      showErrorMessage("boiler_data_set_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["boiler_data_set"] }).then();
  }
})

export const boilerDataSetDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerTypeAdditionalSetController.deleteDataSet,
  onMutate: async (boilerTypeAdditionalSetNew) => {
    await queryClient.cancelQueries({ queryKey: ['boiler_data_set'] });
    const previous = queryClient.getQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set']);
    queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], old => old?.filter((t) => t.id !== boilerTypeAdditionalSetNew));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("boiler_data_set_delete_success", "Набор данных котла удалён");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<BoilerTypeAdditionalDataSetDto[]>(['boiler_data_set'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_data_set_delete_error", error.message);
    } else {
      showErrorMessage("boiler_data_set_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["boiler_data_set"] }).then();
  }
});

export function validateBoilerTypeDataSet(boilerTypeAdditionalDataSetDto: BoilerTypeAdditionalDataSetDto) {
  return {
    name: !validateLength100(boilerTypeAdditionalDataSetDto?.name ?? "")
      ? errorMessageLength100
      : '',
  };
}
