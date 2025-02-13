import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import React, { useMemo, useState } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions,
  useMaterialReactTable
} from 'material-react-table';
import {
  Button,
  Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle,
  FormControl, FormHelperText,
  Input,
  InputAdornment,
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
import { useComponentsNameSet } from 'Frontend/components/api/components_name_set';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import {
  boilerTypeAddMutation, boilerTypeDelete, boilerTypeEditMutation,
  useBoilers,
  validateBoilerType
} from 'Frontend/components/api/boiler_type';
import BoilerTypeDto from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeDto';
import {
  emptyComponentNameSet,
} from 'Frontend/components/api/helper';
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import ComponentsSetDialog from 'Frontend/components/component_set/ComponentsSetDialog';
import DoDisturbIcon from '@mui/icons-material/DoDisturb';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';
import BoilerrTypeAdditionalDialog from 'Frontend/components/boiler_additional_data/BoilerTypeAdditionalDialog';
import { useBoilerDataSet } from 'Frontend/components/api/boiler_type_addition_data_set';

const BoilersType = () => {
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const boilerTypeName = useSignal("");
  const boilerTypeId = useSignal(-1);
  const componentSetOpenDialog = useSignal(false);
  const boilerDataSetOpenDialog = useSignal(false);
  const {data: componentsNameSet, isLoading: isLoadingComponentNameSet,
    isRefetching: isRefetchComponentNameSet} = useComponentsNameSet();
  const {data: boilerDataSets, isLoading: isLoadingBoilerDatSet,
    isRefetching: isRefetchingBoilerDatSet} = useBoilerDataSet();
  const {data: boilers, isError, isLoading, refetch, isRefetching } = useBoilers();
  const { mutateAsync: addBoilerType, isPending: isCreatingBoilerType } = boilerTypeAddMutation(queryClient);
  const { mutateAsync: editBoilerType, isPending: isUpdatingBoilerType } = boilerTypeEditMutation(queryClient);
  const { mutate: deleteBoilerType, isPending: isDeletingBoilerType } = boilerTypeDelete(queryClient);
  const componentNameValueSelected = useSignal<ComponentNameSetDto>({id: undefined, name: ""});
  const boilerSetDataValueSelected = useSignal<BoilerTypeAdditionalDataSetDto>({id: undefined, name: ""});
  const updaterComponentNameSet = useSignal<(componentNameSet: ComponentNameSetDto) => void>((_) => {});
  const updaterBoilerSetData = useSignal<(boilerDataSet: BoilerTypeAdditionalDataSetDto) => void>((_) => {});

  const handleCreateBoilerType: MRT_TableOptions<BoilerTypeDto>['onCreatingRowSave'] = async ({values, table}) => {
    const componentNameSet = componentsNameSet?.find(c => c.name === values['componentNameSet.name']);
    const boilerTypeAdditionalDataSet = boilerDataSets?.find(c => c.name === values['boilerTypeAdditionalDataSet.name']);
    const boilerType = {typeName: values.typeName, model: values.model, article: values.article,
      componentNameSet: componentNameSet || {name: ""},
      boilerTypeAdditionalDataSet: boilerTypeAdditionalDataSet || {name: ""}};
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
    componentNameValueSelected.value = emptyComponentNameSet;
    boilerSetDataValueSelected.value = emptyComponentNameSet;
  };

  const handleSaveBoilerType: MRT_TableOptions<BoilerTypeDto>['onEditingRowSave'] = async ({values, table, row}) => {
    const componentNameSet = componentsNameSet?.find(c => c.name === values['componentNameSet.name']);
    const boilerTypeAdditionalDataSet = boilerDataSets?.find(c => c.name === values['boilerTypeAdditionalDataSet.name']);
    const boilerType = {typeName: values.typeName, model: values.model, article: values.article,
      componentNameSet: componentNameSet || {name: ""},
      boilerTypeAdditionalDataSet: boilerTypeAdditionalDataSet || {name: ""}};
    const newValidationErrors = validateBoilerType(boilerType);
    if (Object.values(newValidationErrors).some((error) => error)) {
      setValidationErrors(newValidationErrors);
      return;
    }
    setValidationErrors({});
    try {
      await editBoilerType(boilerType);
    } catch (error) {}
    table.setEditingRow(null);
    componentNameValueSelected.value = emptyComponentNameSet;
    boilerSetDataValueSelected.value = emptyComponentNameSet;
  };

  const boilersColumn = useMemo<MRT_ColumnDef<BoilerTypeDto>[]>(
    () => [
      {
        accessorKey: 'typeName', //access nested data with dot notation
        header: 'Тип котла',
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
        accessorKey: 'model', //access nested data with dot notation
        header: 'Модель',
        size: 50,
        muiEditTextFieldProps: {
          required: true,
          error: !!validationErrors?.model,
          helperText: validationErrors?.model,
          onFocus: () =>
            setValidationErrors({
              ...validationErrors,
              article: undefined,
            }),
        },
      },
      {
        accessorKey: 'article', //access nested data with dot notation
        header: 'Артикуль',
        size: 30,
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
          const onFocus = () =>
            setValidationErrors({
              ...validationErrors,
              componentNameSet: undefined,
            })

          const callBackUpdaterComponentNameSet = (componentNameSet: ComponentNameSetDto) => {
            row._valuesCache[column.id] = componentNameSet.name;
            componentNameValueSelected.value = componentNameSet;
          }

          return (
            <FormControl variant="standard">
              <Input
                id="component_name_set"
                onFocus={onFocus}
                value={componentNameValueSelected.value.name}
                error={!!validationErrors?.componentNameSet}
                endAdornment={
                  <InputAdornment position="end">
                    <Box>
                        <IconButton edge="end" onClick={() => {
                          updaterComponentNameSet.value = callBackUpdaterComponentNameSet;
                          componentSetOpenDialog.value = true;
                        }}>
                        <MoreHorizIcon />
                      </IconButton>
                    </Box>
                  </InputAdornment>
                }
              />
              <FormHelperText sx={{color: 'red'}} hidden={!validationErrors?.componentNameSet} id="component_name_set_error">
                {validationErrors?.componentNameSet}
              </FormHelperText>
            </FormControl>
          );
        },
      },
      {
        accessorKey: 'boilerTypeAdditionalDataSet.name',
        header: 'Набор данных',
        size: 50,
        Edit: ({ cell, column, row, table }) => {
          const onFocus = () =>
            setValidationErrors({
              ...validationErrors,
              boilerDataSet: undefined,
            })

          const callBackUpdaterBoilerSetData = (boilerDataSet: BoilerTypeAdditionalDataSetDto) => {
            row._valuesCache[column.id] = boilerDataSet.name;
            boilerSetDataValueSelected.value = boilerDataSet;
          }

          return (
            <FormControl variant="standard">
              <Input
                id="boiler_data_set_id"
                onFocus={onFocus}
                value={boilerSetDataValueSelected.value.name}
                error={!!validationErrors?.boilerDataSet}
                endAdornment={
                  <InputAdornment position="end">
                    <Box>
                      <IconButton edge="end" onClick={() => {
                        updaterBoilerSetData.value = callBackUpdaterBoilerSetData;
                        boilerDataSetOpenDialog.value = true;
                      }}>
                        <MoreHorizIcon />
                      </IconButton>
                    </Box>
                  </InputAdornment>
                }
              />
              <FormHelperText sx={{color: 'red'}} hidden={!validationErrors?.boilerDataSet} id="boiler_data_set_error">
                {validationErrors?.boilerDataSet}
              </FormHelperText>
            </FormControl>
          );
        },
      }
    ],
    [validationErrors, componentSetOpenDialog.value, updaterComponentNameSet.value, componentNameValueSelected.value,
      boilerSetDataValueSelected.value, updaterBoilerSetData.value, boilerDataSetOpenDialog.value]
  );

  const table = useMaterialReactTable({
    initialState: { showColumnFilters: true, density: 'compact' },
    columns: boilersColumn,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    state: {
      isLoading: isLoading || isLoadingComponentNameSet,
      isSaving: isCreatingBoilerType || isUpdatingBoilerType || isDeletingBoilerType,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading || isLoadingComponentNameSet || isRefetchComponentNameSet
        || isRefetchComponentNameSet || isLoadingBoilerDatSet || isRefetchingBoilerDatSet,
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
            componentNameValueSelected.value = row.original.componentNameSet;
            table.setEditingRow(row)
          }}>
            <EditIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Удалить">
          <IconButton color="error" onClick={() => {
            boilerTypeName.value = row.original?.typeName ?? "";
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
    onCreatingRowCancel: () => {
      componentNameValueSelected.value = emptyComponentNameSet;
      boilerSetDataValueSelected.value = emptyComponentNameSet;
      setValidationErrors({});
    },
    onCreatingRowSave: handleCreateBoilerType,
    onEditingRowCancel: () => {
      componentNameValueSelected.value = emptyComponentNameSet;
      boilerSetDataValueSelected.value = emptyComponentNameSet;
      setValidationErrors({});
    },
    onEditingRowSave: handleSaveBoilerType,
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <>
      <ComponentsSetDialog key={"component_set_dialog"}
                           dialogOpen={componentSetOpenDialog}
                           updaterComponentNameSet={updaterComponentNameSet} />
      <BoilerrTypeAdditionalDialog key={"boiler_type_additional_set_dialog"}
                                   dialogOpen={boilerDataSetOpenDialog}
                                   updaterBoilerDataSet={updaterBoilerSetData} />
      <Dialog
        open={openDialog.value}
        onClose={() => openDialog.value = false}
        aria-labelledby="alert-dialog-delete_boiler_type_lab"
        aria-describedby="alert-dialog-delete_boiler_type_description">
        <DialogTitle id="alert-dialog-title_delete_boiler_type">
          {"Удаление типа компонента"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description_delete_boiler_type">
            Вы уверены, что хотите удалить тип котла: {boilerTypeName.value}?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{gap: '0.5em'}}>
          <Button color="error"
                  onClick={() => {
                    deleteBoilerType(boilerTypeId.value);
                    openDialog.value = false;
                    boilerTypeName.value = "";
                    boilerTypeId.value = -1;
                  }}
                  startIcon={<DeleteIcon />}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Удалить
          </Button>
          <Button startIcon={<DoDisturbIcon />}
                  onClick={() => {
                    openDialog.value = false
                  }}
                  sx={{maxWidth: '200px'}}
                  variant="contained">
            Отмена
          </Button>
        </DialogActions>
      </Dialog>
      <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em'}} >
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