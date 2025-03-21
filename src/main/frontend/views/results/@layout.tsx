import React from 'react';
import { Outlet, useLocation, useNavigate, Link as RouterLink, useParams } from 'react-router-dom';
import { Breadcrumbs, Link, LinkProps, Typography, Box, IconButton, Tooltip } from '@mui/material';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Suspense } from 'react';
import { useOperationsBySerial } from 'Frontend/components/api/operation';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

// Карта для сопоставления путей с их отображаемыми названиями
const breadcrumbNameMap: { [key: string]: string } = {
  '/results': 'Результаты',
  '/results/boiler': 'Котлы',
  '/results/boiler/order': 'Заказы',
  '/results/boiler/operation': 'Операции',
  '/results/boiler/components': 'Компоненты',
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
  const params = useParams();
  
  // Функция для генерации хлебных крошек
  const generateBreadcrumbs = () => {
    const pathnames = location.pathname.split('/').filter(x => x);
    
    const isOperationPage = pathnames.includes('operation');
    const isComponentsPage = pathnames.includes('components');
    const isOrderPage = pathnames.includes('order') && params.boilerOrderId;
    
    // Получаем параметры из URL
    const serialNumber = params.serialNumber;
    const operationId = params.operationId;
    const boilerOrderId = params.boilerOrderId;

    // Если мы на странице компонентов, получаем данные операции
    const { data: operations } = useOperationsBySerial(serialNumber || '');
    
    const crumbs = [];

    // Добавляем ссылку на котлы для всех дочерних страниц
    if ((isOperationPage && !location.state?.from) || isComponentsPage || (isOrderPage && !boilerOrderId)) {
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
    }

    // Страница заказа
    if (isOrderPage && boilerOrderId) {
      // Если мы на странице операций и пришли из заказа, добавляем ссылку на заказ
      if (location.state?.from === 'order' && location.state?.boilerOrderId) {
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to={`/results/boiler/order/${location.state.boilerOrderId}`}
            key="order"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            Котлы заказа {location.state.boilerOrderId}
          </LinkRouter>
        );
      } else {
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to="/results/boiler/order"
            key="orders"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            Заказ
          </LinkRouter>
        );
        
        crumbs.push(
          <Typography
            key="orderId"
            sx={{ ...breadcrumbItemStyle, fontWeight: 'bold' }}
          >
            Котлы заказа {boilerOrderId}
          </Typography>
        );
      }
    }

    // Страница операций
    if (isOperationPage && serialNumber) {
      // Проверяем, откуда мы пришли на страницу операций
      if (location.state?.from === 'order' && location.state?.boilerOrderId) {
        // Добавляем ссылку на страницу заказов
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to="/results/boiler/order"
            key="orders-list"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            Заказы
          </LinkRouter>
        );
        
        // Уже существующая логика для перехода от заказа
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to={`/results/boiler/order/${location.state.boilerOrderId}`}
            key="order"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            Котлы заказа {location.state.boilerOrderId}
          </LinkRouter>
        );
      } else if (location.state?.from === 'boilers') {
        // Новая логика для перехода со страницы всех котлов
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to="/results/boiler"
            key="boilers-back"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            Котлы
          </LinkRouter>
        );
      }
      
      crumbs.push(
        <Typography
          key="serial"
          sx={{ ...breadcrumbItemStyle, fontWeight: 'bold' }}
        >
          {serialNumber}
        </Typography>
      );
    }

    // Страница компонентов
    if (isComponentsPage && operationId) {
      // На странице компонентов мы можем получить серийный номер из operations или из location state
      // Сначала проверяем location state для серийного номера
      const serialFromState = location.state?.serialNumber;
      
      if (serialFromState) {
        crumbs.push(
          <LinkRouter
            underline="hover"
            color="inherit"
            to={`/results/boiler/operation/${serialFromState}`}
            key="serial"
            sx={{ cursor: 'pointer', ...breadcrumbItemStyle }}
          >
            {serialFromState}
          </LinkRouter>
        );
      }

      // Компоненты с названием рабочего места
      const stationName = location.state?.station;
      crumbs.push(
        <Typography key="components" sx={{ color: 'text.primary', ...breadcrumbItemStyle, fontWeight: 'bold' }}>
          {`Компоненты${stationName ? ` (${stationName})` : ''}`}
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
  rolesAllowed: ["ROLE_Администратор", "ROLE_Бригадир", "ROLE_Мастер/Технолог"],
  title: "Результаты" 
};
