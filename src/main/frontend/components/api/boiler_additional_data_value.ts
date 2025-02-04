import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { BoilerTypeAdditionalValueController } from 'Frontend/generated/endpoints';

export function useBoilerTypeAdditionalValue(id: number, url: string) {
  return useQuery({
    queryKey: ['boiler_additional_value', url],
    queryFn: () => BoilerTypeAdditionalValueController.getAllAdditionalValue(id),
    staleTime: 1000 * 60 * 5,
    enabled: !isNaN(id)
  })
}

export const boilerTypeAdditionalValueEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: BoilerTypeAdditionalValueController.updateAdditionalValue,
  onSuccess: () => {
    showSuccessMessage("boiler_additional_value_edit_success", "Данные обновленны");
  },
  onError: (error, _, context) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("boiler_additional_value_edit_error", error.message);
    } else {
      showErrorMessage("boiler_additional_value_edit_error", "Неизвестная ошибка");
    }
  },
})
