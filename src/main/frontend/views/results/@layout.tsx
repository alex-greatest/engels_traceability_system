import React from 'react';
import { Outlet, useLocation, useNavigate, Link as RouterLink } from 'react-router-dom';
import { Breadcrumbs, Link, LinkProps, Typography, Box } from '@mui/material';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Suspense } from 'react';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

// Карта для сопоставления путей с их отображаемыми названиями
const breadcrumbNameMap: { [key: string]: string } = {
  '/results': 'Результаты',
  '/results/boiler': 'Котлы',
  '/results/boiler/order': 'Заказы',
  '/results/boiler/operation': 'Операции',
};

// Компонент-обертка для удобной навигации
interface LinkRouterProps extends LinkProps {
  to: string;
  replace?: boolean;
}

function LinkRouter(props: LinkRouterProps) {
  return <Link {...props} component={RouterLink} />;
}

// Общие стили для всех элементов хлебных крошек
const breadcrumbItemStyle = {
  fontSize: '1.1rem',
  padding: '4px 6px',
  display: 'flex',
  alignItems: 'center'
};

export default function ResultsLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  
  // Функция для генерации хлебных крошек
  const generateBreadcrumbs = () => {
    const pathnames = location.pathname.split('/').filter(x => x);
    
    // Определяем наличие особых сегментов
    const isOrderPage = pathnames.includes('order');
    const isOperationPage = pathnames.includes('operation');
    
    // Находим серийный номер, исключая служебные сегменты (results, boiler, operation, order)
    const serialNumber = pathnames.find(segment => 
      segment.match(/^[A-Z0-9]+$/i) && 
      !['results', 'boiler', 'operation', 'order'].includes(segment)
    );
    
    // Для страниц верхнего уровня (Котлы и Заказы) не показываем хлебные крошки
    if ((isOrderPage && !serialNumber) || (pathnames.includes('boiler') && !isOperationPage && !serialNumber)) {
      return [];
    }
    
    const crumbs = [];
    
    // Страница операций с серийным номером
    if (isOperationPage && serialNumber) {
      // 1. Ссылка на котлы
      crumbs.push(
        <LinkRouter 
          underline="hover" 
          color="inherit" 
          to="/results/boiler" 
          key="boilers"
          sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
        >
          Котлы
        </LinkRouter>
      );
      
      // 2. Операции (некликабельно)
      crumbs.push(
        <Typography key="operation" sx={{ color: 'text.primary', ...breadcrumbItemStyle }}>
          Операции
        </Typography>
      );
      
      // 3. Серийный номер (некликабельно), отображаем только сам номер
      crumbs.push(
        <Typography key="serial" sx={{ color: 'text.primary', ...breadcrumbItemStyle }}>
          {serialNumber}
        </Typography>
      );
    }
    
    return crumbs;
  };
  
  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column' }}>
      {(() => {
        const breadcrumbs = generateBreadcrumbs();
        return breadcrumbs.length > 0 ? (
          <Box sx={{ padding: '0.8em' }}>
            <Breadcrumbs 
              separator={<NavigateNextIcon fontSize="medium" />} 
              aria-label="breadcrumb"
              sx={{ 
                '& .MuiBreadcrumbs-ol': { alignItems: 'center' },
                '& .MuiBreadcrumbs-separator': { margin: '0 8px' },
                '& .MuiBreadcrumbs-li': { 
                  display: 'flex', 
                  alignItems: 'center',
                  margin: 0
                }
              }}
            >
              {breadcrumbs}
            </Breadcrumbs>
          </Box>
        ) : null;
      })()}
      
      <Suspense fallback="Загрузка...">
        <Outlet />
      </Suspense>
    </Box>
  );
}

export const config: ViewConfig = {
  loginRequired: true,
  rolesAllowed: ["ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"],
  title: "Результаты" 
};
