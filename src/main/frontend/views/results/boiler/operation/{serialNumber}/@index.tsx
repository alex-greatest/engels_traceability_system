import React, { useMemo } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef,
  useMaterialReactTable
} from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import { Container, Box, Tooltip } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { useParams } from 'react-router-dom';
import { useOperationsBySerial } from 'Frontend/components/api/operation';
import dayjs from 'dayjs';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export default function OperationsResults() {
  const { serialNumber } = useParams<{ serialNumber: string }>();
  
  const { data: operations, isError, isLoading, refetch, isRefetching } =
    useOperationsBySerial(serialNumber || '');

  const columns = useMemo<MRT_ColumnDef<any>[]>(
    () => [
      {
        accessorKey: 'id',
        header: 'ID',
        size: 80,
      },
      {
        accessorKey: 'stationName',
        header: 'Станция',
        size: 150,
        Cell: ({ cell }) => {
          const value = cell.getValue<string>();
          switch (value) {
            case 'wp1': return 'Рабочее место 1';
            case 'wp2': return 'Рабочее место 5';
            case 'wp3': return 'Рабочее место 8';
            case 'wp4': return 'Рабочее место 12';
            default: return value;
          }
        },
      },
      {
        accessorKey: 'status',
        header: 'Статус',
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
              displayText = 'Прервана';
              break;
          }

          return (
            <Box component="span" sx={{
              backgroundColor, borderRadius: '0.25rem',
              color: '#fff', padding: '0.25rem 0.5rem',
              fontWeight: 500, display: 'inline-block'
            }}>
              {displayText}
            </Box>
          );
        },
      },
      {
        accessorKey: 'dateCreate',
        header: 'Дата',
        size: 150,
        Cell: ({ cell }) => dayjs(cell.getValue<string>()).format('YYYY-MM-DD HH:mm'),
      }
    ],
    [],
  );

  const table = useMaterialReactTable({
    initialState: {
      showColumnFilters: true,
      density: 'compact',
      sorting: [{ id: 'dateCreate', desc: true }], // Сортировка по дате по умолчанию
    },
    columns,
    localization: MRT_Localization_RU,
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
      <Box sx={{ display: 'flex', gap: '1em', alignItems: 'center' }}>
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
    data: operations || [],
  });

  return (
    <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '1em' }}>
      <MaterialReactTable table={table} />
    </Container>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Операции"
};
