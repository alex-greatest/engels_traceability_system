import React, { useContext, useMemo } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef,
  useMaterialReactTable
} from 'material-react-table';
import {
  Button,
  Container,
  Tooltip
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import { useBoilersByDateRange } from 'Frontend/components/api/boiler';
import dayjs from 'dayjs';
import { DateTimePicker } from '@mui/x-date-pickers';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Context } from 'Frontend/index';
import { useSignal } from '@vaadin/hilla-react-signals';

export default function Boilers() {
  const startDateTimeBoiler = useContext(Context).startDateTimeBoiler;
  const endDateTimeBoiler = useContext(Context).endDateTimeBoiler;
  const submitedStartDate = useSignal(startDateTimeBoiler.value);
  const submitedEndDate = useSignal(endDateTimeBoiler.value);
  
  const { data: boilers, isError, isLoading, refetch, isRefetching } =
    useBoilersByDateRange(submitedStartDate.value, submitedEndDate.value);

  const handleDateChange = () => {
    submitedStartDate.value = startDateTimeBoiler.value;
    submitedEndDate.value = endDateTimeBoiler.value;
  };

  const boilerColumns = useMemo<MRT_ColumnDef<any>[]>(
    () => [
      {
        accessorKey: 'serialNumber',
        header: 'Серийный номер',
        size: 150,
      },
      {
        accessorKey: 'status.name',
        header: 'Последний результат',
        size: 100,
        Cell: ({ cell }) => {
          const value = cell.getValue<string>();
          let displayText = value;
          let backgroundColor = '';
          switch (value) {
            case 'OK':
            backgroundColor = '#4caf50';
            displayText = 'OK';
            break;
            case 'NOK':
              backgroundColor = '#f44336';
              displayText = 'NOK';
              break;
            case 'IN_PROGRESS':
              backgroundColor = '#2196f3';
              displayText = 'В работе';
              break;
              case 'INTERRUPTED':
                backgroundColor = '#ff9800';
              displayText = 'Прервана'; break;
          }

            return (
              <Box component="span" sx={{ backgroundColor, borderRadius: '0.25rem', color: '#fff',
                padding: '0.25rem 0.5rem', fontWeight: 500, display: 'inline-block' }} >
                {displayText}
              </Box>
            );
        },
      },
      {
        accessorKey: 'boilerTypeCycle.typeName',
        header: 'Тип котла',
        size: 150,
      },
      {
        accessorKey: 'boilerTypeCycle.model',
        header: 'Модель',
        size: 150,
      },
      {
        accessorKey: 'boilerTypeCycle.article',
        header: 'Артикул',
        size: 150,
      },
      {
        accessorKey: 'dateCreate',
        header: 'Дата создания',
        size: 150,
        Cell: ({ cell }) => dayjs(cell.getValue<string>()).format('YYYY-MM-DD HH:mm'),
      },
      {
        accessorKey: 'dateUpdate',
        header: 'Дата обновления',
        size: 150,
        Cell: ({ cell }) => {
          const value = cell.getValue<string>();
          return value ? dayjs(value).format('YYYY-MM-DD HH:mm') : '-';
        },
      },
      {
        accessorKey: 'lastStation.name',
        header: 'Последняя станция',
        size: 150,
        Cell: ({ cell }) => {
          const value = cell.getValue<string>();
          switch (value) {
            case 'wp1':
              return 'Рабочее место 1';
            case 'wp2':
              return 'Рабочее место 5';
            case 'wp3':
              return 'Рабочее место 8';
            case 'wp4':
              return 'Рабочее место 12';
            default:
              return value;
          }
        },
      },
      {
        accessorKey: 'userHistory.username',
        header: 'Оператор',
        size: 100,
      },
    ],
    [],
  );

  const table = useMaterialReactTable({
    initialState: {
      showColumnFilters: true,
      density: 'compact',
    },
    columns: boilerColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'last',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    enablePagination: true,
    muiTableContainerProps: { sx: { maxHeight: '650px' } },
    state: {
      isLoading,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading,
    },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{ display: 'flex', gap: '1em' }}>
        <Tooltip arrow title="Обновить данные">
          <IconButton onClick={() => refetch()}>
            <RefreshIcon />
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
    data: boilers || [],
    createDisplayMode: 'row',
    editDisplayMode: 'row',
    enableEditing: false,
    muiTablePaperProps: ({ table }) => ({
      style: {
        zIndex: table.getState().isFullScreen ? 3000 : undefined,
      },
    })
  });

  return (
    <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '5em' }}>
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'flex-end', marginBottom: '1em', }}>
        <Box sx={{ display: 'flex', gap: '1em' }}>
          <DateTimePicker
            label="Начало"
            value={startDateTimeBoiler.value ? dayjs(startDateTimeBoiler.value) : null}
            onChange={(newValue) => startDateTimeBoiler.value = newValue ? newValue.format('YYYY-MM-DDTHH:mm:ss') : ''}
            slotProps={{
              textField: {
                fullWidth: true,
                variant: 'outlined',
                size: 'small',
                sx: { '& .MuiInputBase-root': { height: '40px' } }
              },
            }}
          />
          <DateTimePicker
            label="Конец"
            value={endDateTimeBoiler ? dayjs(endDateTimeBoiler.value) : null}
            onChange={(newValue) => endDateTimeBoiler.value = newValue ? newValue.format('YYYY-MM-DDTHH:mm:ss') : ''}
            slotProps={{
              textField: {
                fullWidth: true,
                variant: 'outlined',
                size: 'small',
                sx: { '& .MuiInputBase-root': { height: '40px' } }
              },
            }} />
          <Button sx={{ minWidth: '150px' }} variant="contained" color="primary" onClick={handleDateChange}>
            Подтвердить
          </Button>
        </Box>
      </Box>
      <MaterialReactTable table={table} />
    </Container>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Котлы"
}; 