import React, { useCallback, useContext, useMemo, useEffect, useState } from 'react';
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
import HistoryIcon from '@mui/icons-material/History';
import { useLocation, useNavigate } from 'react-router-dom';
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
  const navigate = useNavigate();
  const location = useLocation();

  const urlParams = new URLSearchParams(location.search);
  const savedPage = localStorage.getItem('boilersPage');
  const currentPage = parseInt(urlParams.get('page') || savedPage || '0', 10);

  const [pagination, setPagination] = useState({
    pageIndex: currentPage,
    pageSize: 10,
  });

  const handlePageChange = useCallback((pageIndex: number) => {
    const newUrlParams = new URLSearchParams(location.search);
    newUrlParams.set('page', pageIndex.toString());
    localStorage.setItem('boilersPage', pageIndex.toString());
    navigate(`${location.pathname}?${newUrlParams.toString()}`, { replace: true });
  }, [navigate, location.search, location.pathname]);
  
  const { data: boilers, isError, isLoading, refetch, isRefetching } =
    useBoilersByDateRange(submitedStartDate.value, submitedEndDate.value);

  // Эффект для синхронизации пагинации с URL
  useEffect(() => {
    setPagination(prev => ({
      ...prev,
      pageIndex: currentPage
    }));
  }, [currentPage]);

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
        filterVariant: 'select',
        filterFn: 'equals',
        filterSelectOptions: [
          { label: 'OK', value: 'OK' },
          { label: 'NOK', value: 'NOK' },
          { label: 'В работе', value: 'IN_PROGRESS' },
          { label: 'Прервана', value: 'INTERRUPTED' }
        ],
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
        filterVariant: 'select',
        filterFn: 'equals',
        filterSelectOptions: [
          { label: 'Рабочее место 1', value: 'wp1' },
          { label: 'Рабочее место 5', value: 'wp2' },
          { label: 'Рабочее место 8', value: 'wp3' },
          { label: 'Рабочее место 12', value: 'wp4' }
        ],
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
    // Отключаем автоматический сброс пагинации при изменении данных
    autoResetAll: false,
    autoResetPageIndex: false,
    initialState: {
      showColumnFilters: true,
      density: 'compact',
      pagination,
    },
    columns: boilerColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    renderRowActions: ({ row }) => (
      <Box sx={{ display: 'flex', gap: '0.5rem' }}>
        <Tooltip title="История операций">
          <IconButton 
            onClick={() => navigate(`/results/boiler/operation/${row.original.serialNumber}`, {
              state: { from: 'boilers' }
            })}
            color="primary"
          >
            <HistoryIcon />
          </IconButton>
        </Tooltip>
      </Box>
    ),
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    enablePagination: true,
    muiTableContainerProps: { sx: { maxHeight: '650px' } },
    state: {
      isLoading,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading,
      pagination,
    },
    // Обновляем обработчик изменения пагинации
    onPaginationChange: (updater) => {
      const newPagination = typeof updater === 'function' 
        ? updater(pagination) 
        : updater;
      setPagination(newPagination);
      handlePageChange(newPagination.pageIndex);
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
