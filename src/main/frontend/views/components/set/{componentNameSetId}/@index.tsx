import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { VerticalLayout } from '@vaadin/react-components/VerticalLayout.js';
import React, { useMemo, useState } from 'react';
import { FormLayout } from '@vaadin/react-components';
import TextField from '@mui/material/TextField';
import { useQueryClient } from '@tanstack/react-query';
import { useSignal } from '@vaadin/hilla-react-signals'
import { MaterialReactTable, type MRT_ColumnDef, MRT_TableOptions, useMaterialReactTable } from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import { Autocomplete, Button, CircularProgress, Container, Tooltip, Typography } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import {
  componentSetAddMutation, componentSetCopyAllComponentsSetMutation,
  componentSetDelete,
  componentSetEditMutation,
  useComponentSet,
  validateComponentSet,
} from 'Frontend/components/api/components_set';
import { Loading } from 'Frontend/components/config/Loading';
import ComponentTypeDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentTypeDto';
import ComponentSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentSetDto';
import DeleteIcon from '@mui/icons-material/Delete';
import { ConfirmDialog } from '@vaadin/react-components/ConfirmDialog';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { errorMessageLength50, validateLength } from 'Frontend/components/api/helper';

const responsiveSteps = [
  { minWidth: '0', columns: 2 },
  { minWidth: '500px', columns: 2 },
];

const ComponentsSet = () => {
  const url = useLocation().key;
  const navigate = useNavigate();
  const { componentNameSetId } = useParams();
  const {data: componentsSetList, isError, isLoading, refetch, isRefetching } =
    useComponentSet(Number(componentNameSetId), url);
  const queryClient = useQueryClient();
  const openDialog = useSignal(false);
  const openDialogCopyValues = useSignal(false);
  const componentSetName = useSignal("");
  const componentSetId = useSignal(-1);
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
      isEnableEditing.value = true;
      setEditedComponentSet({});
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
      await addComponentSet({componentNameSetId: Number(componentNameSetId) , componentSetDto: componentSetDto});
    } catch (error) {}
    table.setCreatingRow(null);
    isCreatingRowComponentSet.value = false;
    resetEditing();
  };

  const handleEditComponentSet = async () => {
    console.log(editedComponentSet);
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
              const componentSetDto: ComponentSetDto = {id: row.original.id, componentType: componentType || {name: ""},
                value: row.original.value};
              /*const componentSet = componentsSetList?.componentsTypeList.find(c => c.name === event.target.value);
              if (componentSet !== undefined) {
                row.original.componentType = componentSet;
              }*/
              setEditedComponentSet({ ...editedComponentSet, [row.id]: componentSetDto});
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
              defaultValue={row.original.componentType}
              noOptionsText={"Не найдено"}
              renderInput={(params) => <TextField onFocus={onFocus} error={!!validationErrors?.[cell.id]}
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
              const componentSetDto: ComponentSetDto = {id: row.original.id,
                componentType: row.original.componentType || {name: ""}, value: event.target.value};
              const componentType = editedComponentSet[row.id]?.componentType;
              if (componentType !== undefined) {
                componentSetDto.componentType = componentType;
              }
              setEditedComponentSet({ ...editedComponentSet, [row.id]: componentSetDto });
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
    onCreatingRowCancel: () => {setValidationErrors({}); isCreatingRowComponentSet.value = false;},
    onCreatingRowSave: handleCreateComponentSet,
    state: {
      isLoading,
      isSaving: isCreatingComponentSet || isDeletingComponentSet || isEditComponentSet,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading,
    },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{display: 'flex', gap: '1em'}}>
        <Tooltip arrow title="Обновить данные">
          <IconButton onClick={resetEditing} >
            <RefreshIcon />
          </IconButton>
        </Tooltip>
        <Button
          variant="contained"
          color="primary"
          onClick={() => { table.setCreatingRow(true); isCreatingRowComponentSet.value = true }}>
          Создать новый компонент
        </Button>
        <Button
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
            isCreatingRowComponentSet.value ||
            Object.values(validationErrors).some((error) => !!error)
          }
        >
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
      {isLoading ? <Loading /> :
        <>
          <ConfirmDialog
            key={"dialog_copy_values"}
            header='Cкопировать все компонетны'
            cancelButtonVisible
            confirmText="Подвердить"
            cancelText="Отмена"
            confirmTheme="error primary"
            opened={openDialogCopyValues.value}
            onCancel={() => {
              openDialogCopyValues.value = false;
            }}
            onConfirm={() => {
              copyAllComponentSet(Number(componentNameSetId));
              openDialogCopyValues.value = false;
            }}
          >
            <p> Вы уверены, что хотите скопировать компоненты? </p>
          </ConfirmDialog>
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
              deleteComponentSet(componentSetId.value);
              openDialog.value = false;
              componentSetName.value = "";
              componentSetId.value = -1;
            }}
          >
            <p> Вы уверены, что хотите удалить компонент: {componentSetName.value}? </p>
          </ConfirmDialog>
          <VerticalLayout theme="spacing padding">
            <Container maxWidth={"xl"} sx={{display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '2em'}} >
              <FormLayout responsiveSteps={responsiveSteps}>
                <TextField
                  data-colspan="1"
                  label="Название набора"
                  id="outlined-size-small"
                  value={componentsSetList?.componentNameSet.name}
                  size="small"
                  slotProps={{
                    input: {
                      readOnly: true,
                    },
                  }}
                  sx={{backgroundColor: "#1A39601A"}}
                />
              </FormLayout>
              <Box sx={{marginTop: '3em'}}>
                <MaterialReactTable table={table} />
              </Box>
              <Button color="secondary"
                      onClick={() => navigate('/components/name_set')}
                      startIcon={<ArrowBackIcon />}
                      sx={{maxWidth: '250px', marginTop: '3em'}}
                      variant="contained">
                Вернуться к списку
              </Button>
            </Container>
          </VerticalLayout>
        </>
      }
    </>
    );
};

export default ComponentsSet;

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Создание набора компонентов"
};
