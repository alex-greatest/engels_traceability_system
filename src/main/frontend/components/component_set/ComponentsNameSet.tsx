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
  componentNameSetAddMutation, componentNameSetDelete, componentNameSetEditMutation,
  useComponentsNameSet,
  validateComponentNameSet
} from 'Frontend/components/api/components_name_set';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import { validateComponent } from 'Frontend/components/api/components_type';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';

const ComponentsNameSet = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const componentNameSetName = useSignal("");
  const componentNameSetId = useSignal(-1);
  const {data: componentNameSet, isError, isLoading, refetch, isRefetching } = useComponentsNameSet();
  const { mutateAsync: addComponentNameSet, isPending: isCreatingComponentNameSet } = componentNameSetAddMutation(queryClient);
  const { mutateAsync: editComponentNameSet, isPending: isUpdatingComponentNameSet } = componentNameSetEditMutation(queryClient);
  const { mutate: deleteComponentNameSet, isPending: isDeletingComponentNameSet } = componentNameSetDelete(queryClient);

  const handleCreateComponentNameSet: MRT_TableOptions<ComponentNameSetDto>['onCreatingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponentNameSet(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addComponentNameSet(values);
    } catch (error) {}
    table.setCreatingRow(null);
  };

  const handleSaveComponentNameSet: MRT_TableOptions<ComponentNameSetDto>['onEditingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateComponent(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editComponentNameSet({id: componentNameSetId.value, component: values});
    } catch (error) {}
    table.setEditingRow(null);
  };

  const componentNameSetColumns = useMemo<MRT_ColumnDef<ComponentNameSetDto>[]>(
    () => [
      {
        accessorKey: 'name',
        header: 'Название набора компонентов',
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
    columns: componentNameSetColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    state: {
      isLoading,
      isSaving: isCreatingComponentNameSet || isUpdatingComponentNameSet || isDeletingComponentNameSet,
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
          Создать новый набор компонентов
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Изменить название">
          <IconButton onClick={() => {
            componentNameSetName.value = row.original?.name ?? "";
            componentNameSetId.value = row.original?.id ?? -1;
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            componentNameSetName.value = row.original?.name ?? "";
            componentNameSetId.value = row.original?.id ?? -1;
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
    data: componentNameSet || {} as ComponentNameSetDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateComponentNameSet,
    onEditingRowCancel: () => setValidationErrors({}),
    onEditingRowSave: handleSaveComponentNameSet,
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
        aria-labelledby="alert-dialog-delete_component_name_set_lab"
        aria-describedby="alert-dialog-delete_component_name_set_description">
        <DialogTitle id="alert-dialog-title_delete_component_name_set">
          {"Удаление набора компонентов"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_component_name_set">
            Вы уверены, что хотите удалить набор компонентов: {componentNameSetName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteComponentNameSet(componentNameSetId.value);
                    openDialog.value = false;
                    componentNameSetName.value = "";
                    componentNameSetId.value = -1;
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
      <Container maxWidth={'xl'}
                 sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
        <MaterialReactTable table={table} />
      </Container>
    </>
    );

};

export default ComponentsNameSet;