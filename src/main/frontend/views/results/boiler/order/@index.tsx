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
import FormatListBulletedIcon from '@mui/icons-material/FormatListBulleted';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import { useBoilerOrdersByDateRange } from 'Frontend/components/api/boiler_order';
import dayjs from 'dayjs';
import { DateTimePicker } from '@mui/x-date-pickers';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Context } from 'Frontend/index';
import { useSignal } from '@vaadin/hilla-react-signals';
import BoilerOrderDto from 'Frontend/generated/com/rena/application/entity/dto/result/print/BoilerOrderDto';
import { useNavigate } from 'react-router-dom';

export default function BoilerOrders() {
  const navigate = useNavigate();
  const startDateTimeBoilerOrder = useContext(Context).startDateTimeBoilerOrder;
  const endDateTimeBoilerOrder = useContext(Context).endDateTimeBoilerOrder;
  const submitedStartDate = useSignal(startDateTimeBoilerOrder.value);
  const submitedEndDate = useSignal(endDateTimeBoilerOrder.value);
  const { data: boilerOrders, isError, isLoading, refetch, isRefetching } =
    useBoilerOrdersByDateRange(submitedStartDate.value, submitedEndDate.value);

  const handleDateChange = () => {
    submitedStartDate.value = startDateTimeBoilerOrder.value;
    submitedEndDate.value = endDateTimeBoilerOrder.value;
  };

  const boilerOrdersColumns = useMemo<MRT_ColumnDef<BoilerOrderDto>[]>(
    () => [
      {
        accessorKey: 'id',
        header: 'id канбан',
        size: 100,
      },
      {
        accessorKey: 'status.name',
        header: 'Результат',
        size: 100,
        filterVariant: 'select',
        filterFn: 'equals',
        filterSelectOptions: [
          { label: 'OK', value: 'OK' },
          { label: 'NOK', value: 'NOK' },
          { label: 'В работе', value: 'IN_PROGRESS' },
          { label: 'Прервана', value: 'INTERRUPTED' },
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
        size: 100,
      },
      {
        accessorKey: 'orderNumber',
        header: 'Номер заказа',
        size: 100,
      },
      {
        accessorKey: 'amountBoilerOrder',
        header: 'Котлов в заказе',
        size: 200,
      },
      {
        accessorKey: 'amountBoilerPrint',
        header: 'Этикеток распечатано',
        size: 300,
      },
      {
        accessorKey: 'amountBoilerMade',
        header: 'Котлов изготовлено',
        size: 400,
      },
      {
        accessorKey: 'numberShiftCreated',
        header: 'Смена',
        size: 100,
      },
      {
        accessorKey: 'modifiedDate',
        header: 'Дата/время создания',
        size: 100,
        Cell: ({ cell }) => dayjs(cell.getValue<string>()).format('YYYY-MM-DD HH:mm'),
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
      columnVisibility: {
        amountBoilerOrder: false,
        amountBoilerPrint: false,
        amountBoilerMade: false,
        numberShiftCreated: false,
      }
    },
    columns: boilerOrdersColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    enablePagination: true,
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    state: {
      isLoading,
      showAlertBanner: isError,
      showProgressBars: isRefetching || isLoading,
    },
    renderRowActions: ({ row }) => (
      <Box sx={{ display: 'flex', gap: '1rem' }}>
        <Tooltip arrow title="Список котлов">
          <IconButton
            onClick={() => navigate(`/results/boiler/order/${row.original.id}`)}
          >
            <FormatListBulletedIcon />
          </IconButton>
        </Tooltip>
      </Box>
    ),
    renderTopToolbarCustomActions: ({ table }) => (
      <Box sx={{display: 'flex', gap: '1em'}}>
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
    data: boilerOrders || [] as BoilerOrderDto[],
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
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'flex-end', marginBottom: '1em',  }}>
        <Box sx={{display: 'flex', gap: '1em'}}>
          <DateTimePicker
            label="Начало"
            value={startDateTimeBoilerOrder.value ? dayjs(startDateTimeBoilerOrder.value) : null}
            onChange={(newValue) => startDateTimeBoilerOrder.value = newValue ? newValue.format('YYYY-MM-DDTHH:mm:ss') : ''}
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
            value={endDateTimeBoilerOrder ? dayjs(endDateTimeBoilerOrder.value) : null}
            onChange={(newValue) => endDateTimeBoilerOrder.value = newValue ? newValue.format('YYYY-MM-DDTHH:mm:ss') : ''}
            slotProps={{
              textField: {
                fullWidth: true,
                variant: 'outlined',
                size: 'small',
                sx: { '& .MuiInputBase-root': { height: '40px' } }
              },
            }}/>
            <Button sx={{minWidth: '150px'}} variant="contained" color="primary" onClick={handleDateChange}>
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
  title: "Заказы"
};