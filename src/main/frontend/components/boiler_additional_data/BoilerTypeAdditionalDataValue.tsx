import React, { useMemo, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Signal, useSignal } from '@vaadin/hilla-react-signals';
import { MaterialReactTable, type MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import {
  Button,
  CircularProgress,
  Tooltip,
  Typography
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import {
  boilerTypeAdditionalValueEditMutation,
  useBoilerTypeAdditionalValue
} from 'Frontend/components/api/boiler_additional_data_value';
import BoilerTypeAdditionalValueDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalValueDto';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';

interface Props {
  boilerDataSetValue: Signal<BoilerTypeAdditionalDataSetDto>;
}

const BoilerTypeAdditionalDataValue = (props: Props) => {
  const { boilerDataSetValue } = props;
  const boilerDataSetId = Number(boilerDataSetValue.value.id);
  const url = boilerDataSetValue.value.id?.toString() ?? ""
  const queryClient = useQueryClient();
  const { data: boilerTypeAdditionalValues, isError, isLoading, refetch, isRefetching } =
    useBoilerTypeAdditionalValue(boilerDataSetId, url);
  const { mutateAsync: editBoilerTypeAdditionalValue, isPending: isEditBoilerTypeAdditionalValue } =
    boilerTypeAdditionalValueEditMutation(queryClient);
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const [editedBoilerAdditionalValue, setBoilerAdditionalValue] = useState<Record<string, BoilerTypeAdditionalValueDto>>({});
  const isCreatingRowBoilerDataSet = useSignal<boolean>(false);
  const isEnableEditing = useSignal<boolean>(true);

  const resetEditing = () => {
    isEnableEditing.value = false;
    refetch().finally(() => {
      setBoilerAdditionalValue({});
      setValidationErrors({});
      setTimeout(() => {
        refetch().finally(() => isEnableEditing.value = true)
      }, 1000);
    });
  }

  const handleEditComponentSet = async () => {
    if (Object.values(validationErrors).some((error) => !!error)) return;
    await editBoilerTypeAdditionalValue(Object.values(editedBoilerAdditionalValue));
    setBoilerAdditionalValue({});
    resetEditing();
  };

  const componentsSetColumn = useMemo<MRT_ColumnDef<BoilerTypeAdditionalValueDto>[]>(
    () => [
      {
        accessorKey: 'boilerTypeAdditionalData.name',
        header: 'Название',
        size: 100,
        Edit: ({ cell, column, row }) => <>{row.original.boilerTypeAdditionalData.name}</>,
        muiEditTextFieldProps: () => ({
          type: 'text',
          required: true,
          enableEditing: false,
        })
      },
      {
        accessorKey: 'value',
        header: 'Значение',
        size: 50,
        muiEditTextFieldProps: ({ cell, row }) => ({
          type: 'text',
          required: true,
          error: !!validationErrors?.[cell.id],
          helperText: validationErrors?.[cell.id],
          onBlur: (event) => {
            /*const validationError = !validateLength100(event.currentTarget.value)
              ? errorMessageLength100
              : undefined;
            setValidationErrors({
              ...validationErrors,
              [cell.id]: validationError,
            });*/
            row.original.value = event.target.value;
            setBoilerAdditionalValue({ ...editedBoilerAdditionalValue, [row.id]: row.original });
          },
        }),
      },
      {
        accessorKey: 'unit',
        header: 'Значение',
        size: 20,
        muiEditTextFieldProps: ({ cell, row }) => ({
          type: 'text',
          required: true,
          error: !!validationErrors?.[cell.id],
          helperText: validationErrors?.[cell.id],
          onBlur: (event) => {
            /*const validationError = !validateLength30(event.currentTarget.value)
              ? errorMessageLength30
              : undefined;
            setValidationErrors({
              ...validationErrors,
              [cell.id]: validationError,
            });*/
            row.original.value = event.target.value;
            setBoilerAdditionalValue({ ...editedBoilerAdditionalValue, [row.id]: row.original });
          },
        }),
      }
    ],
    [validationErrors, editedBoilerAdditionalValue],
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: componentsSetColumn,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableEditing: isEnableEditing.value,
    createDisplayMode: 'row',
    editDisplayMode: 'table',
    enablePagination: false,
    enableStickyHeader: true,
    enableStickyFooter: true,
    muiTableContainerProps: { sx: { maxHeight: '550px' } },
    onCreatingRowSave: handleEditComponentSet,
    state: {
      isLoading: isLoading,
      isSaving: isEditBoilerTypeAdditionalValue,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading || !isEnableEditing.value,
    },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{display: 'flex', gap: '1em'}}>
        <Tooltip arrow title="Обновить данные">
          <IconButton onClick={resetEditing}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      </Box>
    ),
    renderBottomToolbarCustomActions: () => (
      <Box sx={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        <Button
          color="success"
          variant="contained"
          onClick={handleEditComponentSet}
          disabled={
            Object.keys(editedBoilerAdditionalValue).length === 0 ||
            isCreatingRowBoilerDataSet.value ||
            Object.values(validationErrors).some((error) => !!error)
          }
        >
          {isEditBoilerTypeAdditionalValue ? <CircularProgress size={25} /> : 'Принять изменения'}
        </Button>
        <Button
          color="primary"
          variant="contained"
          onClick={resetEditing}
          disabled={
            Object.keys(editedBoilerAdditionalValue).length === 0 ||
            isCreatingRowBoilerDataSet.value
          }>
          {isEditBoilerTypeAdditionalValue ? <CircularProgress size={25} /> : 'Отменить изменения'}
        </Button>
        {Object.values(validationErrors).some((error) => !!error) && !isCreatingRowBoilerDataSet.value && (
          <Typography color="error">Необходимо устранить все ошибки</Typography>
        )}
      </Box>
    ),
    muiToolbarAlertBannerProps: isError
      ? {
        color: 'error',
        children: 'Ошибка при загрузке данных',
      }
      : undefined,
    data: boilerTypeAdditionalValues || {} as BoilerTypeAdditionalValueDto[],
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <>
      <Box sx={{marginTop: '3em'}}>
        <MaterialReactTable table={table} />
      </Box>
    </>
  );
};

export default BoilerTypeAdditionalDataValue;
