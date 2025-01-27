import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import React, { useMemo, useState } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions,
  useMaterialReactTable
} from 'material-react-table';
import { Autocomplete, Button, Container, Tooltip } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { ConfirmDialog } from '@vaadin/react-components/ConfirmDialog.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useQueryClient } from '@tanstack/react-query';
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import { validateComponent } from 'Frontend/components/api/components_type';
import {
  boilerTypeAddMutation, boilerTypeDelete, boilerTypeEditMutation,
  useBoilers,
  validateBoilerType
} from 'Frontend/components/api/boiler_type';
import BoilerTypeDto from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeDto';
import TextField from '@mui/material/TextField';
import {
  errorMessageEmpty,
  validateRequired
} from 'Frontend/components/api/helper';

const BoilersType = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const boilerTypeName = useSignal("");
  const boilerTypeId = useSignal(-1);
  const {data: componentsNameSet, isLoading: isLoadingComponentNameSet, isRefetching: isRefetchComponentNameSet} = useComponentsNameSet();
  const {data: boilers, isError, isLoading, refetch, isRefetching } = useBoilers();
  const { mutateAsync: addBoilerType, isPending: isCreatingBoilerType } = boilerTypeAddMutation(queryClient);
  const { mutateAsync: editBoilerType, isPending: isUpdatingBoilerType } = boilerTypeEditMutation(queryClient);
  const { mutate: deleteBoilerType, isPending: isDeletingBoilerType } = boilerTypeDelete(queryClient);

  const defaultProps = {
    options: componentsNameSet ?? {} as ComponentNameSetDto[],
    getOptionLabel: (option: ComponentNameSetDto) => option.name,
  };

  const handleCreateBoilerType: MRT_TableOptions<BoilerTypeDto>['onCreatingRowSave'] = async ({values, table}) => {
    const componentNameSet = componentsNameSet?.find(c => c.name === values['componentNameSet.name']);
    const boilerType = {name: values.name, article: values.article, componentNameSet: componentNameSet || {name: ""}};
    const newValidationErrors = validateBoilerType(boilerType);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await addBoilerType(boilerType);
    } catch (error) {}
    table.setCreatingRow(null);
  };

  const handleSaveBoilerType: MRT_TableOptions<BoilerTypeDto>['onEditingRowSave'] = async ({values, table, row}) => {
    const componentNameSet = componentsNameSet?.find(c => c.name === values['componentNameSet.name']);
    const boilerType = {id: Number(row.original.id), name: values.name, article: values.article, componentNameSet: componentNameSet || {name: ""}};
    const newValidationErrors = validateComponent(boilerType);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editBoilerType(boilerType);
    } catch (error) {}
    table.setEditingRow(null);
  };

  const boilersColumn = useMemo<MRT_ColumnDef<BoilerTypeDto>[]>(
    () => [
      {
        accessorKey: 'name', //access nested data with dot notation
        header: 'Название котла',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.name,
          helperText: validationErrors?.name,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              boilers: undefined,
            }),
        },
      },
      {
        accessorKey: 'article', //access nested data with dot notation
        header: 'Идентификатор',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.article,
          helperText: validationErrors?.article,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              article: undefined,
            }),
        },
      },
      {
        accessorKey: 'componentNameSet.name',
        header: 'Набор компонентов',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          const onBlur = (event: any) => {
            row._valuesCache[column.id] = event.target.value;
            const validationError = !validateRequired(event.target.value)
              ? errorMessageEmpty
              : undefined;
            setValidationErrors({
              ...validationErrors,
              componentNameSet: validationError,
            });
          }

          const onFocus = () =>
            setValidationErrors({
              ...validationErrors,
              componentNameSet: undefined,
            })

          return (
            <Autocomplete
              {...defaultProps}
              onBlur={onBlur}
              size="small"
              defaultValue={row.original.componentNameSet}
              noOptionsText={"Не найдено"}
              renderInput={(params) => <TextField onFocus={onFocus} error={!!validationErrors?.componentNameSet}
                                                  helperText={validationErrors?.componentNameSet}
                                                  {...params} />}
            />
          );
        },
      }
    ],
    [validationErrors, defaultProps]
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: boilersColumn,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    state: {
      isLoading: isLoading || isLoadingComponentNameSet,
      isSaving: isCreatingBoilerType || isUpdatingBoilerType || isDeletingBoilerType,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading || isLoadingComponentNameSet || isRefetchComponentNameSet,
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
          Создать новый тип котла
        </Button>
      </Box>
    ),
    renderRowActions: ({ row, table }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip title="Изменить">
          <IconButton onClick={() => {
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            boilerTypeName.value = row.original?.name ?? "";
            boilerTypeId.value = row.original?.id ?? -1;
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
    data: boilers || {} as BoilerTypeDto[],
    createDisplayMode: 'row', // ('modal', and 'custom' are also available)
    editDisplayMode: 'row', // ('modal', 'cell', 'table', and 'custom' are also available)
    enableEditing: true,
    onCreatingRowCancel: () => setValidationErrors({}),
    onCreatingRowSave: handleCreateBoilerType,
    onEditingRowCancel: () => setValidationErrors({}),
    onEditingRowSave: handleSaveBoilerType,
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
        header='Удаление набор компонетнов'
        cancelButtonVisible
        confirmText="Удалить"
        cancelText="Отмена"
        confirmTheme="error primary"
        opened={openDialog.value}
        onCancel={() => {
          openDialog.value = false;
        }}
        onConfirm={() => {
          deleteBoilerType(boilerTypeId.value);
          openDialog.value = false;
          boilerTypeName.value = "";
          boilerTypeId.value = -1;
        }}
      >
        <p> Вы уверены, что хотите удалить тип котла: {boilerTypeName.value}? </p>
      </ConfirmDialog>
      <Container maxWidth={'xl'}
                 sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
        <MaterialReactTable table={table} />
      </Container>
    </>
    );

};

export default BoilersType;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Типы котлов"
};