import React, { useMemo } from 'react';
import { MaterialReactTable, type MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import { Box, Container, Tooltip } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import { useNavigate, useParams } from 'react-router-dom';
import { useOperationsBySerial } from 'Frontend/components/api/operation';
import dayjs from 'dayjs';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export default function OperationsResults() {
  const { serialNumber } = useParams<{ serialNumber: string }>();
  const navigate = useNavigate();
  
  const { data: operations, isError, isLoading, refetch, isRefetching } =
    useOperationsBySerial(serialNumber || '');

  // Функция для преобразования идентификатора станции в читаемое имя
  function getStationName(stationId: string): string {
    switch (stationId) {
      case 'wp1': return 'Рабочее место 1';
      case 'wp2': return 'Рабочее место 5';
      case 'wp3': return 'Рабочее место 8';
      case 'wp4': return 'Рабочее место 12';
      default: return stationId;
    }
  }

  function showComponentsIcon(nameStation: string): boolean {
    const allowedStations = ['wp2', 'wp3', 'wp4'];
    return allowedStations.includes(nameStation);
  }

  const columns = useMemo<MRT_ColumnDef<any>[]>(() => [
      {
        accessorKey: 'id',
        header: 'ID',
        size: 80,
      },
      {
        accessorKey: 'stationName',
        header: 'Станция',
        size: 150,
        filterVariant: 'select',
        filterFn: 'equals',
        filterSelectOptions: [
          { label: 'Рабочее место 1', value: 'wp1' },
          { label: 'Рабочее место 5', value: 'wp2' },
          { label: 'Рабочее место 8', value: 'wp3' },
          { label: 'Рабочее место 12', value: 'wp4' }
        ],
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
      sorting: [{ id: 'dateCreate', desc: true }],
    },
    columns,
    localization: MRT_Localization_RU,
    paginationDisplayMode: 'pages',
    enableStickyHeader: true,
    enableStickyFooter: true,
    enablePagination: true,
    enableRowActions: true,
    positionActionsColumn: 'first',
    renderRowActions: ({ row }) => (
      <>
        { showComponentsIcon(row.original.stationName) && 
                <Box sx={{ display: 'flex', gap: '0.5rem' }}>
                <Tooltip title="Компоненты">
                  <IconButton
                    onClick={() => navigate(`/results/boiler/components/${row.original.id}`, {
                      state: {
                        station: getStationName(row.original.stationName),
                        serialNumber: serialNumber
                      }
                    })}
                    color="primary"
                  >
                    <OpenInNewIcon />
                  </IconButton>
                </Tooltip>
              </Box>
      }
      </>
    ),
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
