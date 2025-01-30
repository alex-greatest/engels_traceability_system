import React, { useMemo, useState } from 'react';
import TextField from '@mui/material/TextField';
import { QueryObserverResult, RefetchOptions, useQueryClient } from '@tanstack/react-query';
import { Signal, useSignal } from '@vaadin/hilla-react-signals';
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
  Tooltip,
  Typography
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import {
  componentSetAddMutation, componentSetCopyAllComponentsSetMutation,
  componentSetDelete,
  componentSetEditMutation, useComponentSet,
  validateComponentSet
} from 'Frontend/components/api/components_set';
import ComponentTypeDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentTypeDto';
import ComponentSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentSetDto';
import DeleteIcon from '@mui/icons-material/Delete';
import { emptyComponentNameSet, errorMessageLength50, validateLength } from 'Frontend/components/api/helper';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';

interface Props {
  componentNameValue: Signal<ComponentNameSetDto>;
}

const ComponentsSetTable = (props: Props) => {
  const { componentNameValue } = props;
  const componentNameSetId = Number(componentNameValue.value.id);
  const url = componentNameValue.value.id?.toString() ?? ""
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const openDialogCopyValues = useSignal(false);
  const componentSetName = useSignal("");
  const componentSetId = useSignal(-1);
  const { data: componentsSetList, isError, isLoading, refetch, isRefetching } = useComponentSet(componentNameSetId, url);
  const { mutateAsync: addComponentSet, isPending: isCreatingComponentSet } = componentSetAddMutation(queryClient, url);
  const { mutateAsync: editComponentSet, isPending: isEditComponentSet } = componentSetEditMutation(queryClient, url);
  const { mutate: deleteComponentSet, isPending: isDeletingComponentSet } = componentSetDelete(queryClient, url);
  const { mutate: copyAllComponentSet, isPending: isCopyAllComponentSet } = componentSetCopyAllComponentsSetMutation(queryClient, url);
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const [editedComponentSet, setEditedComponentSet] = useState<Record<string, ComponentSetDto>>({});
  const isCreatingRowComponentSet = useSignal<boolean>(false);
  const isEnableEditing = useSignal<boolean>(true);

  const defaultProps = {
    options: componentsSetList?.componentsTypeList ?? {} as ComponentTypeDto[],
    getOptionLabel: (option: ComponentTypeDto) => option.name,
  };

  const resetEditing = () => {
    isEnableEditing.value = false;
    refetch().finally(() => {
      setEditedComponentSet({});
      setValidationErrors({});
      setTimeout(() => {
        refetch().finally(() => isEnableEditing.value = true)
      }, 1000);
    });
  }

  const handleCreateComponentSet: MRT_TableOptions<ComponentSetDto>['onCreatingRowSave'] = async ({values, table}) => {
    const componentSetDto: ComponentSetDto = {componentType: {name: values['componentType.name']}, value: values.value};
    const newValidationErrors = validateComponentSet(componentSetDto);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addComponentSet({componentNameSetId, componentSetDto: componentSetDto});
    } catch (error) {}
    table.setCreatingRow(null);
    isCreatingRowComponentSet.value = false;
    resetEditing();
  };

  const handleEditComponentSet = async () => {
    if (Object.values(validationErrors).some((error) => !!error)) return;
    await editComponentSet(Object.values(editedComponentSet));
    setEditedComponentSet({});
    resetEditing();
  };

  const componentsSetColumn = useMemo<MRT_ColumnDef<ComponentSetDto>[]>(
    () => [
      {
        accessorKey: 'componentType.name',
        header: 'Название компонента',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          const onBlur = (event: any) => {
            row._valuesCache[column.id] = event.target.value;
            if (!isCreatingRowComponentSet.value) {
              const validationError = !validateLength(event.target.value)
                ? errorMessageLength50
                : undefined;
              setValidationErrors({
                ...validationErrors,
                [cell.id]: validationError,
              });
              const componentType = componentsSetList?.componentsTypeList.find(c => c.name === event.target.value);
              row.original.componentType = componentType || {name: ""};
              setEditedComponentSet({ ...editedComponentSet, [row.id]: row.original});
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
        accessorKey: 'value',
        header: 'Значение',
        size: 50,
        muiEditTextFieldProps: ({ cell, row }) => ({
          required: true,
          error: !!validationErrors?.[cell.id],
          helperText: validationErrors?.[cell.id],
          onBlur: (event) => {
            if (!isCreatingRowComponentSet.value) {
              const validationError = !validateLength(event.target.value)
                ? errorMessageLength50
                : undefined;
              setValidationErrors({
                ...validationErrors,
                [cell.id]: validationError,
              });
              row.original.value = event.target.value;
              setEditedComponentSet({ ...editedComponentSet, [row.id]: row.original });
            }
          },
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              valueComponent: undefined,
            }),
        }),
      }
    ],
    [validationErrors, defaultProps, isCreatingRowComponentSet.value, editedComponentSet, componentsSetList?.componentsTypeList],
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
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    onCreatingRowCancel: () => {setValidationErrors({}); isCreatingRowComponentSet.value = false;},
    onCreatingRowSave: handleCreateComponentSet,
    state: {
      isLoading: isLoading,
      isSaving: isCreatingComponentSet || isDeletingComponentSet || isEditComponentSet || isCopyAllComponentSet,
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
        <Button
          disabled={isNaN(componentNameSetId)}
          variant="contained"
          color="primary"
          onClick={() => { table.setCreatingRow(true); isCreatingRowComponentSet.value = true }}>
          Создать новый компонент
        </Button>
        <Button
          disabled={isNaN(componentNameSetId)}
          variant="contained"
          color="primary"
          onClick={() => { openDialogCopyValues.value = true }}>
          Скопировать все компонетны
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            componentSetName.value = row.original?.componentType.name ?? "";
            componentSetId.value = row.original?.id ?? -1;
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
            isCreatingRowComponentSet.value ||
            Object.values(validationErrors).some((error) => !!error)
          }
        >
          {isEditComponentSet ? <CircularProgress size={25} /> : 'Принять изменения'}
        </Button>
        <Button
          color="primary"
          variant="contained"
          onClick={resetEditing}
          disabled={
            Object.keys(editedComponentSet).length === 0 ||
            isCreatingRowComponentSet.value
          }>
          {isEditComponentSet ? <CircularProgress size={25} /> : 'Отменить изменения'}
        </Button>
        {Object.values(validationErrors).some((error) => !!error) && !isCreatingRowComponentSet.value && (
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
    data: componentsSetList?.componentsSet || {} as ComponentSetDto[],
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <>
      <Dialog
        open={openDialogCopyValues.value}
        onClose={() => openDialogCopyValues.value = false}
        aria-labelledby="alert-dialog-copy_component_name_set_lab"
        aria-describedby="alert-dialog-copy_component_name_set_description">
        <DialogTitle id="alert-dialog-title_copy_component_name_set">
          {"Копирование всех типов компонентов"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_copy_component_set">
            Вы уверены, что хотите скопировать компоненты?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    copyAllComponentSet(componentNameSetId);
                    openDialogCopyValues.value = false;
                  }}
                  startIcon={<DeleteIcon />}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Копировать
          </Button>
          <Button startIcon={<DoDisturbIcon />}
                  onClick={() => {
                    openDialogCopyValues.value = false;
                  }}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Отмена
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={openDialog.value}
        onClose={() => openDialog.value = false}
        aria-labelledby="alert-dialog-delete_component_set_lab"
        aria-describedby="alert-dialog-delete_component_set_description">
        <DialogTitle id="alert-dialog-title_delete_component_set">
          {"Удаление компонента"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_delete_component_set">
            Вы уверены, что хотите удалить компонент: {componentSetName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteComponentSet(componentSetId.value);
                    openDialog.value = false;
                    componentSetName.value = "";
                    componentSetId.value = -1;
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
      <Box sx={{marginTop: '3em'}}>
        <MaterialReactTable table={table} />
      </Box>
    </>
  );
};

export default ComponentsSetTable;
