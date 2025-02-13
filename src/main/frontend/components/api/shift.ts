import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { ShiftController } from 'Frontend/generated/endpoints';
import ShiftDto from 'Frontend/generated/com/rena/application/entity/dto/ShiftDto';

export function useShifts() {
  return useQuery({
    queryKey: ['shifts'],
    queryFn: ShiftController.getAllShifts,
    staleTime: 1000 * 60 * 5
  })
}

export const shiftAddMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ShiftController.addShift,
  onMutate: async (newShift) => {
    await queryClient.cancelQueries({ queryKey: ['shifts'] });
    const previous = queryClient.getQueryData<ShiftDto[]>(['shifts']);
    queryClient.setQueryData<ShiftDto[]>(['shifts'], old => [...(old as ShiftDto[]), (newShift as ShiftDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("shift_add_success", "Смена добавлена");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ShiftDto[]>(['shifts'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("shift_add_error", error.message);
    } else {
      showErrorMessage("shift_add_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["shifts"] }).then();
  }
});

export const shiftEditMutation = (queryClient: QueryClient) => useMutation({
  mutationFn: ({ oldShiftNumber, shift }: { oldShiftNumber: number; shift: ShiftDto }) =>
    ShiftController.updateShift(oldShiftNumber, shift),
  onMutate: async (shiftNew) => {
    await queryClient.cancelQueries({ queryKey: ['shifts'] });
    const previous = queryClient.getQueryData<ShiftDto[]>(['shifts']);
    queryClient.setQueryData<ShiftDto[]>(['shifts'], old => [...(old as ShiftDto[]), (shiftNew.shift as ShiftDto)]);
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("shift_edit_success", "Смена обновленна");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<ShiftDto[]>(['shifts'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("shift_edit_error", error.message);
    } else {
      showErrorMessage("shift_edit_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["shifts"] }).then();
  }
})

export const shiftDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: ShiftController.deleteShift,
  onMutate: async (shiftNew) => {
    await queryClient.cancelQueries({ queryKey: ['shifts'] });
    const previous = queryClient.getQueryData<ShiftDto[]>(['shifts']);
    queryClient.setQueryData<ShiftDto[]>(['shifts'], old => old?.filter((t) => t.number !== shiftNew));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("shift_delete_success", "Смена удаленна");
  },
  onError: (error, _, context) => {
    queryClient.setQueryData<ShiftDto[]>(['shifts'], context?.previous);
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("shift_delete_error", error.message);
    } else {
      showErrorMessage("shift_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["shifts"] }).then();
  }
});

export function validateShift(shift: ShiftDto) {
  return {
    number: shift.number <= 0
      ? "Номер смены должен быть больше нуля"
      : '',
    timeStart: shift.timeStart === null || shift.timeStart === "" ? "Необходимо установить время" : '',
    timeEnd: shift.timeEnd === null || shift.timeEnd === "" ? "Необходимо установить время" : ''
  };
}
