import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Box,
  Paper
} from '@mui/material';
import { useComponentsByOperationId } from './api/components_result_print';
import ComponentResult from 'Frontend/generated/com/rena/application/entity/dto/result/print/ComponentResult';

interface ComponentsDetailPanelProps {
  operationId: number;
}

export const ComponentsDetailPanel: React.FC<ComponentsDetailPanelProps> = ({ operationId }) => {
  const { data: componentsResult, isError, isLoading } = useComponentsByOperationId(operationId);
  const components = componentsResult?.components ?? [] as ComponentResult[];

  if (isLoading) {
    return <div>Загрузка...</div>;
  }

  if (isError) {
    return <div>Ошибка при загрузке данных</div>;
  }

  return (
    <Box sx={{ margin: '0.1em' }}>
      <TableContainer 
        component={Paper} 
        sx={{ 
          backgroundColor: 'rgba(0, 0, 0, 0.02)',
          '& .MuiPaper-root': {
            boxShadow: 'none'
          }
        }}
      >
        <Table size="small">
          <TableHead>
            <TableRow sx={{ 
              '& th': { 
                backgroundColor: 'rgba(0, 0, 0, 0.08)',
                fontWeight: 600
              }
            }}>
              <TableCell>Наименование</TableCell>
              <TableCell>Значение</TableCell>
              <TableCell>Статус</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {components.map((component, index) => (
              <TableRow 
                key={index}
                sx={{
                  '&:nth-of-type(odd)': {
                    backgroundColor: 'rgba(0, 0, 0, 0.02)',
                  },
                }}
              >
                <TableCell>{component.name}</TableCell>
                <TableCell>{component.value}</TableCell>
                <TableCell>
                  <Box component="span" sx={{
                    backgroundColor: 
                      component.status?.name === 'OK' ? '#4caf50' :
                      component.status?.name === 'NOK' ? '#f44336' :
                      component.status?.name === 'IN_PROGRESS' ? '#2196f3' :
                      component.status?.name === 'INTERRUPTED' ? '#ff9800' : '',
                    borderRadius: '0.25rem',
                    color: '#fff',
                    padding: '0.25rem 0.5rem',
                    fontWeight: 500,
                    display: 'inline-block'
                  }}>
                    {component.status?.name === 'OK' ? 'OK' :
                     component.status?.name === 'NOK' ? 'NOK' :
                     component.status?.name === 'IN_PROGRESS' ? 'В работе' :
                     component.status?.name === 'INTERRUPTED' ? 'Прервана' :
                     component.status?.name}
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default ComponentsDetailPanel;
