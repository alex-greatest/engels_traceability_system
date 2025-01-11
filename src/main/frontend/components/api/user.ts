import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { UserService } from 'Frontend/generated/endpoints';
import { showErrorMessage, showSuccessMessage } from 'Frontend/components/config/notification';
import { EndpointError } from '@vaadin/hilla-frontend';
import { NavigateFunction } from 'react-router-dom';
import UserRequestUpdate from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestUpdate';
import UserRequestPassword from 'Frontend/generated/com/rena/application/entity/dto/user/UserRequestPassword';
import UserResponse from 'Frontend/generated/com/rena/application/entity/dto/user/UserResponse';

export function useUsers() {
  return useQuery({
    queryKey: ['users'],
    queryFn: UserService.getAllUsers,
    staleTime: 1000 * 60 * 5
  })
}

export const usersAddMutation = (queryClient: QueryClient, navigate: NavigateFunction) => useMutation({
  mutationFn: UserService.addUser,
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
    showSuccessMessage("users_add_success", "Пользователь успешно добавлен");
    navigate("/users");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("DbException")) {
      showErrorMessage("users_add_error", error.message);
    } else {
      showErrorMessage("users_add_error", "Неизвестная ошибка");
    }
  },
});

export function useUser(code: number) {
  return useQuery({
    queryKey: ['user_edit'],
    queryFn: () => UserService.getUser(code ?? 0),
    enabled: !isNaN(code)
  })
}

export const usersEditMutation = (queryClient: QueryClient, navigate: NavigateFunction) => useMutation({
  mutationFn: ({ code, user }: { code: number; user: UserRequestUpdate }) => UserService.updateUser(code, user),
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
  mutationFn: ({ code, user }: { code: number; user: UserRequestPassword }) => UserService.updatePasswordUser(code, user),
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
  mutationFn: UserService.deleteUser,
  onMutate: async (newTodo) => {
    await queryClient.cancelQueries({ queryKey: ['users'] });
    const previousTodos = queryClient.getQueryData<UserResponse>(['users']);
    queryClient.setQueryData<UserResponse[]>(['users'], old => old?.filter((t) => t.code !== newTodo));
    return { previousTodos }
  },
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["users"] }).then();
    showSuccessMessage("users_delete_success", "Пользователь успешно удалён");
  },
  onError: (error) => {
    if (error instanceof EndpointError && error.type?.includes("DbException")) {
      showErrorMessage("users_delete_error", error.message);
    } else {
      showErrorMessage("users_delete_error", "Неизвестная ошибка");
    }
  },
});

