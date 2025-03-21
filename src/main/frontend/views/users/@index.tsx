import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import React, { useMemo } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef,
  useMaterialReactTable
} from 'material-react-table';
import { Button, Container, Tooltip } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { userDelete, useUsers } from 'Frontend/components/api/user';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import { useNavigate } from 'react-router-dom';
import UserResponse from 'Frontend/generated/com/rena/application/entity/dto/user/UserResponse';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PasswordIcon from '@mui/icons-material/Password';
import { ConfirmDialog } from '@vaadin/react-components/ConfirmDialog.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useQueryClient } from '@tanstack/react-query';

const Users = () => {
  const {data: users, isError, isLoading, refetch, isRefetching } = useUsers();
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const openDialog = useSignal(false);
  const username = useSignal("");
  const codeUser = useSignal(-1);
  const mutationDelete = userDelete(queryClient);

  const usersColumns = useMemo<MRT_ColumnDef<UserResponse>[]>(
    () => [
      {
        accessorKey: 'username', //access nested data with dot notation
        header: 'Логин',
        size: 50,
      },
      {
        accessorKey: 'code',
        header: 'Код сотрудника',
        size: 10,
      },
      {
        accessorKey: 'role.name',
        header: 'Роль',
        size: 10,
      }
    ],
    [],
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: usersColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    state: {
      isLoading,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading,
    },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{display: 'flex', gap: '1em'}}>
        <Tooltip arrow title="Обновить данные">
          <IconButton onClick={() => refetch()}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
        <Button
          variant="contained"
          color="primary"
          onClick={() => {
            navigate('/users/add')
          }}
        >
          Создать нового пользователя
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Редактировать">
          <IconButton onClick={() => navigate(`/users/edit/${row.original.code}`)}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        {row.original.role?.name !== "Оператор" &&
          <Tooltip title="Сменить пароль">
          <IconButton onClick={() => navigate(`/users/edit/password/${row.original.code}`)}>
            <PasswordIcon />
          </IconButton>
        </Tooltip>
        }
        <Tooltip title="Удалить">
          <IconButton onClick={() => {
            openDialog.value = true;
            username.value = row.original.username ?? "";
            codeUser.value = row.original.code ?? -1;
          }} color="error">
            <DeleteIcon />
          </IconButton>
        </Tooltip>
      </Box>
    ),
    muiToolbarAlertBannerProps: isError
      ? {
        color: 'error',
        children: 'Ошибка при загрузке данных',
      }
      : undefined,
    data: users || {} as UserResponse[],
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <>
      <ConfirmDialog
        key={"dialog"}
        header='Удаление пользователя'
        cancelButtonVisible
        confirmText="Удалить"
        confirmTheme="error primary"
        opened={openDialog.value}
        onCancel={() => {
          openDialog.value = false;
        }}
        onConfirm={() => {
          mutationDelete.mutate(codeUser.value);
          openDialog.value = false;
          username.value = "";
          codeUser.value = -1;
        }}
      >
        <p> Вы уверены, что хотите удалить пользователя {username.value} с кодом: {codeUser.value} ? </p>
      </ConfirmDialog>
      <Container maxWidth={"xl"} sx={{display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
        <MaterialReactTable table={table} />
      </Container>
    </>
    );

};

export default Users;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Бригадир", "ROLE_Мастер/Технолог"],
  title: "Пользователи"
};
