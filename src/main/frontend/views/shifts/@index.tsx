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
  shiftAddMutation,
  shiftDelete,
  shiftEditMutation,
  useShifts,
  validateShift
} from '../../components/api/shift';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { TimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import ShiftDto from '../../generated/com/rena/application/entity/dto/shift/ShiftDto';

export default function ShiftsView() {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const shiftNumber = useSignal(-1);
  const timeStartValue = useSignal("");
  const timeEndValue = useSignal("");
  const {data: shifts, isError, isLoading, refetch, isRefetching } = useShifts();
  const { mutateAsync: addShift, isPending: isCreatingShift } = shiftAddMutation(queryClient);
  const { mutateAsync: editShift, isPending: isUpdatingShift } = shiftEditMutation(queryClient);
  const { mutate: deleteShift, isPending: isDeletingShift } = shiftDelete(queryClient);

  const handleCreateShift: MRT_TableOptions<ShiftDto>['onCreatingRowSave'] = async ({values, table}) => {
    const shift = {number: values.number, timeStart:values['Начало смены'], timeEnd: values['Конец смены']};
    const newValidationErrors = validateShift(shift);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addShift(shift);
    } catch (error) {}
    table.setCreatingRow(null);
  };

  const handleSaveShift: MRT_TableOptions<ShiftDto>['onEditingRowSave'] = async ({values, table}) => {
    const shift = {number: values.number, timeStart:values['Начало смены'], timeEnd: values['Конец смены']};
    const newValidationErrors = validateShift(shift);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editShift({oldShiftNumber: shiftNumber.value, shift: shift});
    } catch (error) {}
    table.setEditingRow(null);
    timeStartValue.value = "";
    timeEndValue.value = "";
  };

  const shiftsColumns = useMemo<MRT_ColumnDef<ShiftDto>[]>(
    () => [
      {
        accessorKey: 'number',
        header: 'Номер смены',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.number,
          helperText: validationErrors?.number,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              number: undefined,
            }),
        },
      },
      {
        accessorFn: (row) => dayjs(`2022-04-17T${row.timeStart == undefined || row.timeStart === ""
          ? '00:00' : row.timeStart}`).format('HH:mm'),
        header: 'Начало смены',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          return (
            <TimePicker
              value={dayjs(`2022-04-17T${row.original.timeStart == undefined || row.original.timeStart === ""
                ? '00:00' : row.original.timeStart}`)}
              format="HH:mm"
              onChange={(newValue) => {
                row.original.timeStart = newValue?.format('HH:mm') ?? '00:00';
                row._valuesCache[column.id] = newValue?.format('HH:mm') ?? '00:00';
              }}
              slotProps={{
                textField: {
                  fullWidth: true,
                  variant: 'standard',
                  error: !!validationErrors?.timeStart,
                  helperText: validationErrors?.timeStart,
                },
              }}
            />
          );
        },
      },
      {
        accessorFn: (row) => dayjs(`2022-04-17T${row.timeEnd == undefined || row.timeEnd === ""
          ? '00:00' : row.timeEnd}`).format('HH:mm'),
        header: 'Конец смены',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          return (
            <TimePicker
              format="HH:mm"
              value={dayjs(`2022-04-17T${row.original.timeEnd == undefined || row.original.timeEnd === "" 
                ? '00:00' : row.original.timeEnd}`)}
              onChange={(newValue) => {
                row.original.timeEnd = newValue?.format('HH:mm') ?? '00:00';
                row._valuesCache[column.id] = newValue?.format('HH:mm') ?? '00:00';
              }}
              slotProps={{
                textField: {
                  fullWidth: true,
                  variant: 'standard',
                  error: !!validationErrors?.timeEnd,
                  helperText: validationErrors?.timeEnd,
                },
              }}
            />
          );
        },
      }
    ],
    [validationErrors],
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: shiftsColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    enablePagination: false,
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    state: {
      isLoading,
      isSaving: isCreatingShift || isUpdatingShift || isDeletingShift,
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
          Создать новую смену
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Изменить смену">
          <IconButton onClick={() => {
            timeStartValue.value = row.original.timeStart ?? "";
            timeEndValue.value = row.original.timeEnd ?? "";
            shiftNumber.value = row.original?.number ?? -1;
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            shiftNumber.value = row.original?.number ?? -1;
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
    data: shifts || {} as ShiftDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateShift,
    onEditingRowCancel: (row) => {
      row.row.original.timeStart = dayjs(`2022-04-17T${timeStartValue.value}`).format('HH:mm');
      row.row.original.timeEnd = dayjs(`2022-04-17T${timeEndValue.value}`).format('HH:mm');
      setValidationErrors({});
    },
    onEditingRowSave: handleSaveShift,
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
        aria-labelledby="shift_lab"
        aria-describedby="shift_description">
        <DialogTitle id="alert-dialog-shift">
          {"Удаление смены"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_shift">
            Вы уверены, что хотите удалить смену: {shiftNumber.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteShift(shiftNumber.value);
                    openDialog.value = false;
                    shiftNumber.value = -1;
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

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Настройка смен"
};