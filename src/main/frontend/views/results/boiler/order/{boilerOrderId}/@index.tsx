import React, { useMemo } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef,
  useMaterialReactTable
} from 'material-react-table';
import {
  Container,
  Tooltip
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import HistoryIcon from '@mui/icons-material/History';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import Box from '@mui/material/Box';
import { useBoilersByOrderId } from 'Frontend/components/api/boiler';
import dayjs from 'dayjs';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useParams, useNavigate } from 'react-router-dom';
import BoilerResult from 'Frontend/generated/com/rena/application/entity/dto/result/print/BoilerResult';

export default function BoilersOfOrderResults() {
  const { boilerOrderId } = useParams();
  const orderId = boilerOrderId ? parseInt(boilerOrderId) : 0;
  const navigate = useNavigate();

  const { data: boilers, isError, isLoading, refetch, isRefetching } =
    useBoilersByOrderId(orderId);

  const boilersColumns = useMemo<MRT_ColumnDef<BoilerResult>[]>(
    () => [
      {
        accessorKey: 'serialNumber',
        header: 'Серийный номер',
        size: 150,
      },
      {
        accessorKey: 'status.name',
        header: 'Статус',
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
        size: 150,
      },
      {
        accessorKey: 'lastStation.name',
        header: 'Последняя станция',
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
        Cell: ({ cell }) => dayjs(cell.getValue<string>()).format('YYYY-MM-DD HH:mm'),
      },
      {
        accessorKey: 'userHistory.username',
        header: 'Оператор',
        size: 150,
      },
    ],
    [],
  );

  const table = useMaterialReactTable({
    initialState: {
      showColumnFilters: true,
      density: 'compact',
      columnVisibility: {
        dateCreate: false,
      }
    },
    columns: boilersColumns,
    localization: MRT_Localization_RU,
    positionActionsColumn: 'first',
    enableRowActions: true,
    renderRowActions: ({ row }) => (
      <Box sx={{ display: 'flex', gap: '0.5rem' }}>
        <Tooltip title="История операций">
          <IconButton 
            onClick={() => navigate(`/results/boiler/operation/${row.original.serialNumber}`, {
              state: { from: 'order', boilerOrderId: orderId }
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
    muiTableContainerProps: { sx: { maxHeight: '1000px' } },
    state: {
      isLoading,
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
      </Box>
    ),
    muiToolbarAlertBannerProps: isError
      ? {
        color: 'error',
        children: 'Ошибка при загрузке данных',
      }
      : undefined,
    data: boilers || [] as BoilerResult[],
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
    <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '2em' }}>
      <MaterialReactTable table={table} />
    </Container>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Котлы заказа"
};
