import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { NavigateFunction } from 'react-router-dom';
import UserRequestUpdate from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestUpdate';
import UserRequestPassword from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestPassword';
import UserResponse from 'Frontend/generated/com/rena/application/entity/dto/user/UserResponse';
import { UserController } from 'Frontend/generated/endpoints';

export function useUsers() {
  return useQuery({
    queryKey: ['users'],
    queryFn: UserController.getAllUsers,
    staleTime: 1000 * 60 * 5
  })
}

export const usersAddMutation = (queryClient: QueryClient, navigate: NavigateFunction) => useMutation({
  mutationFn: UserController.addUser,
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
    showSuccessMessage("users_add_success", "Пользователь успешно добавлен");
    navigate("/users");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("users_add_error", error.message);
    } else {
      showErrorMessage("users_add_error", "Неизвестная ошибка");
    }
  },
});

export function useUser(code: number) {
  return useQuery({
    queryKey: ['user_edit'],
    queryFn: () => UserController.getUser(code ?? 0),
    enabled: !isNaN(code)
  })
}

export const usersEditMutation = (queryClient: QueryClient, navigate: NavigateFunction) => useMutation({
  mutationFn: ({ code, user }: { code: number; user: UserRequestUpdate }) => UserController.updateUser(code, user),
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
    showSuccessMessage("users_edit_success", "Данные пользователя обновленны");
    navigate("/users");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("users_edit_error", error.message);
    } else {
      showErrorMessage("users_edit_error", "Неизвестная ошибка");
    }
  },
})

export const usersEditPasswordMutation = (queryClient: QueryClient, navigate: NavigateFunction) => useMutation({
  mutationFn: ({ code, user }: { code: number; user: UserRequestPassword }) => UserController.updatePasswordUser(code, user),
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
    showSuccessMessage("users_edit_password_success", "Данные пользователя обновленны");
    navigate("/users");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("users_edit_password_error", error.message);
    } else {
      showErrorMessage("users_edit_password_error", "Неизвестная ошибка");
    }
  },
})

export const userDelete = (queryClient: QueryClient) => useMutation({
  mutationFn: UserController.deleteUser,
  onMutate: async (newUser) => {
    await queryClient.cancelQueries({ queryKey: ['users'] });
    const previous = queryClient.getQueryData<UserResponse[]>(['users']);
    queryClient.setQueryData<UserResponse[]>(['users'], old => old?.filter((t) => t.code !== newUser));
    return { previous }
  },
  onSuccess: () => {
    showSuccessMessage("users_delete_success", "Пользователь успешно удалён");
  },
  onError: (error, _, context) => {
    if (context?.previous) {
      queryClient.setQueryData<UserResponse[]>(['users'], context.previous)
    }
    if (error instanceof EndpointError && error.type?.includes("com.rena.application.exceptions")) {
      showErrorMessage("users_delete_error", error.message);
    } else {
      showErrorMessage("users_delete_error", "Неизвестная ошибка");
    }
  },
  onSettled: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
  }
});

