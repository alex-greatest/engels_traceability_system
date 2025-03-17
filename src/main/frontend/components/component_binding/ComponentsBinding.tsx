import React, { useContext, useEffect, useMemo, useState } from 'react';
import TextField from '@mui/material/TextField';
import { useQueryClient } from '@tanstack/react-query';
import { Signal, useSignal, useSignalEffect } from '@vaadin/hilla-react-signals';
import { MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions, useMaterialReactTable } from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import {
  Autocomplete,
  Button,
  CircularProgress,
  Dialog, DialogActions,
  DialogContent, DialogContentText,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Stack,
  Tooltip,
  Select,
  Typography
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import ComponentTypeDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentTypeDto';
import DeleteIcon from '@mui/icons-material/Delete';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import {
  componentBindingAddMutation, componentBindingDelete, componentBindingEditMutation,
  useComponentsBinding,
  useComponentsTypeSet,
  validateComponentBinding
} from 'Frontend/components/api/component_binding';
import { Context } from 'Frontend/index';
import { errorMessageLength50, validateLength } from 'Frontend/components/api/helper';
import ComponentBindingResponse
  from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentBindingResponse';
import ComponentBindingRequest
  from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentBindingRequest';

interface Props {
  componentNameValue: Signal<ComponentNameSetDto>;
}

const ComponentsBinding = (props: Props) => {
  const { componentNameValue } = props;
  const queryClient = useQueryClient();
  const isAddNewComponentType = useContext(Context).isAddNewComponentType;
  const isComponentSetDeleted = useContext(Context).isComponentSetDeleted;
  const isComponentSetUpdated = useContext(Context).isComponentSetUpdated;
  const componentNameSetId = Number(componentNameValue.value.id);
  const stationNameValue = useSignal<string>("wp2");
  const openDialog = useSignal(false);
  const componentBindingName = useSignal("");
  const componentBindingId = useSignal(-1);
  const { data: componentsBinding, isError, isLoading, refetch, isRefetching } =
    useComponentsBinding(componentNameSetId, stationNameValue.value);
  const { data: componentsTypeSet, isError: isErrorComponentType, isLoading: isLoadingComponentType,
    refetch: refetchComponentType, isRefetching: isRefetchingComponentType } =
    useComponentsTypeSet(componentNameSetId, componentNameValue.value.name);
  const { mutateAsync: addComponentBinding, isPending: isCreatingComponentBinding } =
    componentBindingAddMutation(queryClient, componentNameSetId, stationNameValue.value);
  const { mutateAsync: editComponentBinding, isPending: isEditComponentBinding } = componentBindingEditMutation(queryClient);
  const { mutate: deleteComponentBinding, isPending: isDeletingComponentBinding } =
    componentBindingDelete(queryClient, componentNameSetId, stationNameValue.value);
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const [editedComponentSet, setEditedComponentSet] = useState<Record<string, ComponentBindingRequest>>({});
  const isCreatingRowComponentBinding = useSignal<boolean>(false);
  const isEnableEditing = useSignal<boolean>(true);

  const defaultProps = {
    options: componentsTypeSet ?? {} as ComponentTypeDto[],
    getOptionLabel: (option: ComponentTypeDto) => option.name,
  };

  useSignalEffect(() => {
    if (componentNameValue.value.id !== undefined && !isNaN(Number(componentNameValue.value.id))) {
      isEnableEditing.value = false;
      setTimeout(() => isEnableEditing.value = true);
    }
    if (isAddNewComponentType.value) {
      isAddNewComponentType.value = false;
      refetchComponentType().then(() => {});
    }
    if (isComponentSetDeleted.value) {
      isComponentSetDeleted.value = false;
      resetEditing();
    }
    if (isComponentSetUpdated.value) {
      isComponentSetUpdated.value = false;
      resetEditing();
    }
  });

  const resetEditing = () => {
    isEnableEditing.value = false;
    isCreatingRowComponentBinding.value = false;
    setEditedComponentSet({});
    setValidationErrors({});
    setTimeout(() => isEnableEditing.value = true, 1000);
    refetch().then();
  }

  const handleCreateComponentSet: MRT_TableOptions<ComponentBindingResponse>['onCreatingRowSave'] = async ({values, table}) => {
    console.log(table);
    const componentBindingRequest: ComponentBindingRequest =
      {stationName: stationNameValue.value, componentNameSet: componentNameValue.value,
        componentType: {name: values['componentType.name']}, order: values.order};
    const newValidationErrors = validateComponentBinding(componentBindingRequest);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addComponentBinding(componentBindingRequest);
    } catch (error) {}
    table.setCreatingRow(null);
    isCreatingRowComponentBinding.value = false;
    resetEditing();
  };

  const handleEditComponentSet = async () => {
    if (Object.values(validationErrors).some((error) => !!error)) return;
    await editComponentBinding(Object.values(editedComponentSet));
    setEditedComponentSet({});
    resetEditing();
  };

  const componentsSetColumn = useMemo<MRT_ColumnDef<ComponentBindingResponse>[]>(
    () => [
      {
        accessorKey: 'componentType.name',
        header: 'Название компонента',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          const onBlur = (event: any) => {
            row._valuesCache[column.id] = event.target.value;
            if (!isCreatingRowComponentBinding.value) {
              const validationError = !validateLength(event.target.value)
                ? errorMessageLength50
                : undefined;
              setValidationErrors({
                ...validationErrors,
                [cell.id]: validationError,
              });
              const componentType = componentsTypeSet?.find(c => c.name === event.target.value);
              row.original.componentType = componentType || {name: ""};
              const componentBindingRequest: ComponentBindingRequest =
                {id: row.original.id ?? 0,
                  stationName: stationNameValue.value,
                  componentNameSet: componentNameValue.value,
                  componentType: row.original.componentType, order: row.original.order};
              setEditedComponentSet({ ...editedComponentSet, [row.id]: componentBindingRequest });
            }
          };

          const onFocus = () =>
            setValidationErrors({
              ...validationErrors,
              nameComponent: undefined,
            })

          return (
            <Autocomplete
              {...defaultProps}
              onBlur={onBlur}
              size="small"
              value={row.original.componentType}
              noOptionsText={"Не найдено"}
              renderInput={(params) => <TextField variant="standard" onFocus={onFocus} error={!!validationErrors?.[cell.id]}
                                                  helperText={validationErrors?.[cell.id]}
                                                  {...params} />}
            />
          );
        },
      },
      {
        accessorKey: 'order',
        header: 'Порядок',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.order,
          helperText: validationErrors?.order,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              order: undefined,
            }),
        },
      }
    ],
    [isCreatingRowComponentBinding.value, validationErrors, componentsTypeSet, editedComponentSet, defaultProps, stationNameValue.value, componentNameValue.value],
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: componentsSetColumn,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableRowActions: true,
    enableEditing: isEnableEditing.value,
    createDisplayMode: 'row',
    editDisplayMode: 'table',
    enablePagination: false,
    enableStickyHeader: true,
    enableStickyFooter: true,
    muiTableContainerProps: { sx: { maxHeight: '300px' } },
    onCreatingRowCancel: () => {setValidationErrors({}); isCreatingRowComponentBinding.value = false;},
    onCreatingRowSave: handleCreateComponentSet,
    state: {
      isLoading: isLoading,
      isSaving: isCreatingComponentBinding || isDeletingComponentBinding || isEditComponentBinding,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading || !isEnableEditing.value,
    },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{display: 'flex', gap: '1em'}}>
        <Tooltip arrow title="Обновить данные">
          <span>
            <IconButton disabled={isNaN(componentNameSetId) || componentNameSetId === null} onClick={resetEditing}>
              <RefreshIcon />
            </IconButton>
          </span>
        </Tooltip>
        <Button
          disabled={isNaN(componentNameSetId)}
          variant="contained"
          color="primary"
          onClick={() => { table.setCreatingRow(true); isCreatingRowComponentBinding.value = true }}>
          Создать новую привязку
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            componentBindingName.value = row.original?.componentType.name ?? "";
            componentBindingId.value = row.original?.id ?? -1;
            openDialog.value = true;
          }}>
            <DeleteIcon />
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
            Object.keys(editedComponentSet).length === 0 ||
            isCreatingRowComponentBinding.value ||
            Object.values(validationErrors).some((error) => !!error)
          }
        >
          {isEditComponentBinding ? <CircularProgress size={25} /> : 'Принять изменения'}
        </Button>
        <Button
          color="primary"
          variant="contained"
          onClick={resetEditing}
          disabled={
            Object.keys(editedComponentSet).length === 0 ||
            isCreatingRowComponentBinding.value
          }>
          {isEditComponentBinding ? <CircularProgress size={25} /> : 'Отменить изменения'}
        </Button>
        {Object.values(validationErrors).some((error) => !!error) && !isCreatingRowComponentBinding.value && (
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
    data: componentsBinding || {} as ComponentBindingResponse[],
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
          {"Удаление привязки"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_delete_component_set">
            Вы уверены, что хотите удалить компонент: {componentBindingName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteComponentBinding(componentBindingId.value);
                    openDialog.value = false;
                    componentBindingName.value = "";
                    componentBindingId.value = -1;
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
      <Stack sx={{marginTop: '5em'}}>
        <Box sx={{display: 'flex'}}>
          <Typography component="div" sx={{fontSize: '20px', marginTop: 'auto'}}> Привязка компонентов к рабочему месту </Typography>
          <FormControl sx={{ m: 1, minWidth: 120, marginLeft: 'auto' }} size="small">
            <InputLabel id="select-station-label">Станция</InputLabel>
            <Select
              id="station-select-small"
              label={"Станция"}
              value={stationNameValue.value}
              sx={{backgroundColor: '#1A39601A', minWidth: '300px'}}
              onChange={(event) => stationNameValue.value = event.target.value}
            >
              <MenuItem value={"wp2"}>Рабочее место 5</MenuItem>
              <MenuItem value={"wp3"}>Рабочее место 8</MenuItem>
              <MenuItem value={"wp4"}>Рабочее место 12</MenuItem>
            </Select>
          </FormControl>
        </Box>
        <MaterialReactTable table={table} />
      </Stack>
    </>
  );
};

export default ComponentsBinding;
