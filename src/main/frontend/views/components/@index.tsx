import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import React, { useMemo, useState } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions,
  useMaterialReactTable
} from 'material-react-table';
import { Button, Container, Tooltip } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { ConfirmDialog } from '@vaadin/react-components/ConfirmDialog.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useQueryClient } from '@tanstack/react-query';
import {
  componentAddMutation,
  componentDelete, componentEditMutation,
  useComponents,
  validateComponent
} from 'Frontend/components/api/components';
import ComponentDto from 'Frontend/generated/com/rena/application/entity/dto/user/ComponentDto';

const Components = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const componentName = useSignal("");
  const componentId = useSignal(-1);
  const {data: components, isError, isLoading, refetch, isRefetching } = useComponents();
  const { mutateAsync: addComponent, isPending: isCreatingComponent } = componentAddMutation(queryClient);
  const { mutateAsync: editComponent, isPending: isUpdatingComponent } = componentEditMutation(queryClient);
  const { mutate: deleteComponent, isPending: isDeletingComponent } = componentDelete(queryClient);
  
  const handleCreateComponent: MRT_TableOptions<ComponentDto>['onCreatingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponent(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      console.log(newValidationErrors);
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    await addComponent(values);
    table.setCreatingRow(null);
  };

  const handleSaveUser: MRT_TableOptions<ComponentDto>['onEditingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponent(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    await editComponent({id: values.id, oldName: componentName.value, component: values});
    componentName.value = "";
    table.setEditingRow(null);
  };

  const componentsColumn = useMemo<MRT_ColumnDef<ComponentDto>[]>(
    () => [
      {
        accessorKey: 'name',
        header: 'Название компонента',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.name,
          helperText: validationErrors?.name,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              firstName: undefined,
            }),
        },
      }
    ],
    [validationErrors],
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: componentsColumn,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    state: {
      isLoading,
      isSaving: isCreatingComponent || isUpdatingComponent || isDeletingComponent,
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
          onClick={() => table.setCreatingRow(true)}>
          Создать новый компонент
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Edit">
          <IconButton onClick={() => {
            componentName.value = row.original?.name ?? "";
            componentId.value = row.original?.id ?? -1;
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Delete">
          <IconButton color="error" onClick={() => {
            componentName.value = row.original?.name ?? "";
            componentId.value = row.original?.id ?? -1;
            openDialog.value = true;
          }}>
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
    data: components || {} as ComponentDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateComponent,
    onEditingRowCancel: () => setValidationErrors({}),
    onEditingRowSave: handleSaveUser,
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
          deleteComponent(componentId.value);
          openDialog.value = false;
          componentName.value = "";
          componentId.value = -1;
        }}
      >
        <p> Вы уверены, что хотите удалить компонент: {componentName.value} </p>
      </ConfirmDialog>
      <Container maxWidth={"xl"} sx={{display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
        <MaterialReactTable table={table} />
      </Container>
    </>
    );

};

export default Components;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Комопненты"
};
