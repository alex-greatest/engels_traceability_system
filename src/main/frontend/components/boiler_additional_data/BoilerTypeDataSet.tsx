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
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import {
  boilerDataSetAddMutation, boilerDataSetDelete,
  boilerDataSetEditMutation,
  useBoilerDataSet, validateBoilerTypeDataSet
} from 'Frontend/components/api/boiler_type_addition_data_set';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';

const BoilerTypeDataSet = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const boilerTypeDataSetName = useSignal("");
  const boilerTypeDataSetId = useSignal(-1);
  const {data: boilerTypeDatasSet, isError, isLoading, refetch, isRefetching } = useBoilerDataSet();
  const { mutateAsync: addBoilerTypeDataSet, isPending: isCreatingBoilerTypeDataSet } = boilerDataSetAddMutation(queryClient);
  const { mutateAsync: editBoilerTypeDataSet, isPending: isUpdatingBoilerTypeDataSet } = boilerDataSetEditMutation(queryClient);
  const { mutate: deleteBoilerTypeDataSet, isPending: isDeletingBoilerTypeDataSet } = boilerDataSetDelete(queryClient);

  const handleCreateBoilerTypeDataSet: MRT_TableOptions<BoilerTypeAdditionalDataSetDto>['onCreatingRowSave'] = async ({values, table}) => {
    const newValidationErrors = validateBoilerTypeDataSet(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addBoilerTypeDataSet(values);
    } catch (error) {}
    table.setCreatingRow(null);
  };

  const handleSaveBoilerTypeDataSet: MRT_TableOptions<BoilerTypeAdditionalDataSetDto>['onEditingRowSave'] = async ({values, table, row}) => {
    const newValidationErrors = validateBoilerTypeDataSet(values);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editBoilerTypeDataSet({ id: row.original.id, name: values.name });
    } catch (error) {}
    table.setEditingRow(null);
  };

  const componentNameSetColumns = useMemo<MRT_ColumnDef<BoilerTypeAdditionalDataSetDto>[]>(
    () => [
      {
        accessorKey: 'name',
        header: 'Название набора данных',
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
      isSaving: isCreatingBoilerTypeDataSet || isUpdatingBoilerTypeDataSet || isDeletingBoilerTypeDataSet,
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
          Создать новый набор данных котла
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Изменить название">
          <IconButton onClick={() => {
            boilerTypeDataSetName.value = row.original?.name ?? "";
            boilerTypeDataSetId.value = row.original?.id ?? -1;
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            boilerTypeDataSetName.value = row.original?.name ?? "";
            boilerTypeDataSetId.value = row.original?.id ?? -1;
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
    data: boilerTypeDatasSet || {} as BoilerTypeAdditionalDataSetDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateBoilerTypeDataSet,
    onEditingRowCancel: () => setValidationErrors({}),
    onEditingRowSave: handleSaveBoilerTypeDataSet,
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
        aria-labelledby="alert-dialog-delete_boiler_type_set_lab"
        aria-describedby="alert-dialog-delete_boiler_type_set_description">
        <DialogTitle id="alert-dialog-title_delete_boiler_type_set">
          {"Удаление набора данных котла"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_component_name_set">
            Вы уверены, что хотите удалить набор данных котла: {boilerTypeDataSetName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteBoilerTypeDataSet(boilerTypeDataSetId.value);
                    openDialog.value = false;
                    boilerTypeDataSetName.value = "";
                    boilerTypeDataSetId.value = -1;
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

export default BoilerTypeDataSet;