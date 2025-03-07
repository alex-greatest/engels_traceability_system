import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import React, { useMemo, useState } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions,
  useMaterialReactTable
} from 'material-react-table';
import {
  Button,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Tooltip
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useQueryClient } from '@tanstack/react-query';
import {
  componentAddMutation,
  componentDelete, componentEditMutation,
  useComponents,
  validateComponent
} from 'Frontend/components/api/components_type';
import ComponentTypeDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentTypeDto';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';

const ComponentsType = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const componentName = useSignal("");
  const componentId = useSignal(-1);
  const {data: components, isError, isLoading, refetch, isRefetching } = useComponents();
  const { mutateAsync: addComponent, isPending: isCreatingComponent } = componentAddMutation(queryClient);
  const { mutateAsync: editComponent, isPending: isUpdatingComponent } = componentEditMutation(queryClient);
  const { mutate: deleteComponent, isPending: isDeletingComponent } = componentDelete(queryClient);
  
  const handleCreateComponent: MRT_TableOptions<ComponentTypeDto>['onCreatingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponent(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addComponent(values);
    } catch (error) {}
    table.setCreatingRow(null);
  };

  const handleSaveComponent: MRT_TableOptions<ComponentTypeDto>['onEditingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponent(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editComponent({id: componentId.value, component: values});
    } catch (error) {}
    table.setEditingRow(null);
  };

  const componentsColumn = useMemo<MRT_ColumnDef<ComponentTypeDto>[]>(
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
    enableStickyHeader: true,
    enableStickyFooter: true,
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
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
          Создать новый тип компонента
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
    data: components || {} as ComponentTypeDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateComponent,
    onEditingRowCancel: () => setValidationErrors({}),
    onEditingRowSave: handleSaveComponent,
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <>
      <Dialog
        open={openDialog.value}
        onClose={() => openDialog.value = false}
        aria-labelledby="alert-dialog-delete_component_set_lab"
        aria-describedby="alert-dialog-delete_component_set_description">
        <DialogTitle id="alert-dialog-title_delete_component_set">
          {"Удаление типа компонента"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_delete_component_set">
            Вы уверены, что хотите удалить компонент: {componentName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteComponent(componentId.value);
                    openDialog.value = false;
                    componentName.value = "";
                    componentId.value = -1;
                  }}
                  startIcon={<DeleteIcon />}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Удалить
          </Button>
          <Button startIcon={<DoDisturbIcon />}
                  onClick={() => {
                    openDialog.value = false;
                  }}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Отмена
          </Button>
        </DialogActions>
      </Dialog>
      <Container maxWidth={"xl"} sx={{display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
        <MaterialReactTable table={table} />
      </Container>
    </>
    );

};

export default ComponentsType;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Комопненты"
};
