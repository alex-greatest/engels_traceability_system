import React, { useMemo } from 'react';
import {
  MaterialReactTable, type MRT_ColumnDef,
  useMaterialReactTable
} from 'material-react-table';
import { MRT_Localization_RU } from 'material-react-table/locales/ru';
import { Container, Box, Tooltip, Typography } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useComponentsByOperationId } from 'Frontend/components/api/components_result_print';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import ComponentResult from 'Frontend/generated/com/rena/application/entity/dto/result/print/ComponentResult';


export default function ComponentsResults() {
  const navigate = useNavigate();
  const location = useLocation();
  const { operationId } = useParams<{ operationId: string }>();
  const parsedOperationId = operationId ? Number(operationId) : undefined;
  const isValidOperationId = !isNaN(Number(operationId));
  const { data: componentsResult, isError, isLoading, refetch, isRefetching } =
    useComponentsByOperationId(isValidOperationId ? parsedOperationId! : 0);
  const components = componentsResult?.components ?? [] as ComponentResult[];

  React.useEffect(() => {
    if (isValidOperationId && componentsResult?.station) {
      navigate(`/results/boiler/components/${operationId}`, {
        replace: true,
        state: { 
          station: componentsResult.station,
          serialNumber: location.state?.serialNumber || ''
        },
      });
    }
  }, [componentsResult?.station, isValidOperationId, navigate, operationId, location.state?.serialNumber]);


  const columns = useMemo<MRT_ColumnDef<ComponentResult>[]>(
    () => [
      {
        accessorKey: 'name',
        header: 'Наименование',
        size: 200,
      },
      {
        accessorKey: 'value',
        header: 'Значение',
        size: 100,
      },
      {
        accessorKey: 'status.name',
        header: 'Статус',
        size: 100,
        filterVariant: 'select',
        filterFn: 'equals', 
        filterSelectOptions: [
          { text: 'OK', value: 'OK' },
          { text: 'NOK', value: 'NOK' },
          { text: 'В работе', value: 'IN_PROGRESS' },
          { text: 'Прервана', value: 'INTERRUPTED' }
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
    positionActionsColumn: 'last',
    muiTableContainerProps: { sx: { maxHeight: '650px' } },
    state: {
      isLoading,
      showAlertBanner: isError || !isValidOperationId,
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
    muiToolbarAlertBannerProps: isError || !isValidOperationId
      ? {
        color: 'error',
        children: !isValidOperationId 
          ? 'Некорректный ID операции' 
          : 'Ошибка при загрузке данных',
      }
      : undefined,
    data: components ?? [],
  });

  return (
    <Container maxWidth={'xl'} sx={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', marginTop: '1em' }}>
      <Typography variant={'h5'} sx={{fontWeight: 'bold'}}>
        {`${componentsResult?.station ?? ""}`}
      </Typography>
      <MaterialReactTable table={table} />
    </Container>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Компоненты"
};
