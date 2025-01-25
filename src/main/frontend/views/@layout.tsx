import { useViewConfig } from '@vaadin/hilla-file-router/runtime.js';
import { effect, signal, useSignal } from '@vaadin/hilla-react-signals';
import { AppLayout, Button, DrawerToggle, Icon, SideNav, SideNavItem, Tooltip } from '@vaadin/react-components';
import { Suspense, useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from 'Frontend/components/config/auth/auth';


const documentTitleSignal = signal('');
effect(() => {
  document.title = documentTitleSignal.value;
});

// Publish for Vaadin to use
(window as any).Vaadin.documentTitleSignal = documentTitleSignal;

export default function MainLayout() {
  const currentTitle = useViewConfig()?.title;
  const navigate = useNavigate();
  const location = useLocation();
  const isCollapsed = useSignal(true);
  const { logout } = useAuth();

  useEffect(() => {
    if (currentTitle) {
      documentTitleSignal.value = currentTitle;
    }
  }, [currentTitle]);

  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
            <div className="ml-m">
                <svg style={{color: 'red'}} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 215 24">
                    <symbol xmlns="http://www.w3.org/2000/svg" viewBox="0 0 215 24" fill="none"
                            id="05225f63-e93c-4929-9754-30177d866851">
                        <path
                            d="M206.542 11.053a3.65 3.65 0 01-1.326.242h-.01a3.606 3.606 0 01-1.316-.268 3.08 3.08 0 01-1.056-.693 3.26 3.26 0 01-.697-1.05 3.575 3.575 0 010-2.626 3.151 3.151 0 01.703-1.05 3.006 3.006 0 011.056-.693c.42-.166.868-.248 1.32-.242.453-.005.904.077 1.326.242a2.956 2.956 0 011.056.693c.301.3.538.657.697 1.05.338.852.338 1.8 0 2.652-.16.392-.397.749-.697 1.05-.301.3-.66.537-1.056.693zM180.115 8.51h2.054a2.652 2.652 0 001.753-.498 1.94 1.94 0 00.588-1.386 1.924 1.924 0 00-.588-1.386 2.72 2.72 0 00-1.753-.483h-2.054V8.51z"
                            fill="currentColor"/>
                        <path fillRule="evenodd" clipRule="evenodd"
                              d="M214.247 0h-68.46v16.172h68.46V0zm-11.075 12.465c.653.247 1.346.37 2.044.363a5.518 5.518 0 002.023-.378 5.108 5.108 0 001.616-1.014c.457-.43.817-.953 1.056-1.533a5.1 5.1 0 000-3.864 4.51 4.51 0 00-1.056-1.534 4.912 4.912 0 00-1.616-.997 5.888 5.888 0 00-4.067 0 4.957 4.957 0 00-1.621 1.013 4.514 4.514 0 00-1.056 1.533 5.086 5.086 0 000 3.86c.241.578.601 1.1 1.056 1.533.47.44 1.02.785 1.621 1.018zm-50.335-7.71v7.934h1.759V4.755h3.137v-1.48H149.7v1.48h3.137zm6.026 7.934h1.759V8.656h4.896v4.033h1.758V3.274h-1.758V7.16h-4.896V3.274h-1.759v9.415zm12.469-1.465h5.339v1.465h-7.119V3.274h6.929V4.74h-5.149v2.452h4.558V8.63h-4.558v2.594zm7.019-7.948v9.415h1.764V9.928h2.133c.122.003.243.001.364-.004l1.939 2.765h1.901l-2.194-3.103.145-.057a3.09 3.09 0 001.389-1.145 3.207 3.207 0 00.486-1.78 3.253 3.253 0 00-.507-1.77 3.166 3.166 0 00-1.389-1.155 5.24 5.24 0 00-2.155-.404h-3.876zm9.686 9.413V3.274h1.447l3.767 6.238 3.695-6.238h1.447v9.415h-1.647V6.447l-3.127 5.165h-.781l-3.111-5.075v6.162l-1.69-.01zM106.384 24a14.243 14.243 0 01-5.117-.893 12.114 12.114 0 01-4.067-2.52 11.467 11.467 0 01-2.667-3.787 11.854 11.854 0 01-.95-4.773 11.82 11.82 0 01.95-4.772 11.437 11.437 0 012.689-3.818 12.33 12.33 0 014.066-2.52 14.869 14.869 0 0110.145-.026 12.29 12.29 0 014.035 2.52 11.898 11.898 0 012.683 3.796c.658 1.513.987 3.147.966 4.794a11.784 11.784 0 01-.961 4.8 11.512 11.512 0 01-2.683 3.811 12.421 12.421 0 01-4.04 2.49 13.892 13.892 0 01-5.054.892l.005.005zm-.031-4.574a7.68 7.68 0 002.883-.525 6.914 6.914 0 002.338-1.542 6.856 6.856 0 001.528-2.339 7.91 7.91 0 00.555-3.014 7.939 7.939 0 00-.555-3.019 7.063 7.063 0 00-1.531-2.352 6.8 6.8 0 00-2.319-1.528 7.798 7.798 0 00-2.899-.525 7.68 7.68 0 00-2.884.525 6.953 6.953 0 00-3.871 3.88 8.029 8.029 0 00-.528 3.02 8.056 8.056 0 00.528 2.998 7.058 7.058 0 001.537 2.373 6.678 6.678 0 002.313 1.523 7.854 7.854 0 002.905.525z"
                              fill="currentColor"/>
                        <path
                            d="M13.214 14.806L21.806.398h4.436l.069 23.208h-5.07l-.032-13.93-6.87 11.472h-2.435L5.07 9.975v13.63H0V.399h4.468l8.746 14.408zm33.614-5.159H29.215v4.69h17.613v-4.69zm0 9.234H29.215v4.69h17.613v-4.69z"
                            fill="currentColor"/>
                        <path fillRule="evenodd" clipRule="evenodd"
                              d="M137.481 16.18l5.212 7.446h-5.835l-4.484-6.49h-4.949v6.49h-5.402V.397h10.103a13.238 13.238 0 015.381 1.008 7.934 7.934 0 013.502 2.904 8.093 8.093 0 011.23 4.51 7.95 7.95 0 01-1.23 4.458 7.833 7.833 0 01-3.481 2.904h-.047zm-.968-8.993a3.743 3.743 0 00-.954-1.355l-.011-.01c-.824-.694-2.065-1.051-3.734-1.051h-4.399v8.091h4.399c1.669 0 2.91-.357 3.734-1.076a3.738 3.738 0 001.236-2.966 3.722 3.722 0 00-.271-1.633z"
                              fill="currentColor"/>
                        <path
                            d="M49.833.394v4.725h7.468v18.483h5.403V5.093h7.467v-4.7H49.833zM29.215.401h17.64v4.7h-17.64V.4zm61.569 0h-17.64v4.7h17.64V.4zm-17.64 9.246h17.613v4.69H73.144v-4.69zm17.613 9.234H73.144v4.69h17.613v-4.69z"
                            fill="currentColor"/>
                    </symbol>
                    <use href="#05225f63-e93c-4929-9754-30177d866851"/>
                </svg>
            </div>

            <SideNav color={"--lumo-primary-color"} onNavigate={({path}) => navigate(path!)} location={location}>
              <SideNavItem path={'/users'} key={"index"}>
                <Icon icon="vaadin:users" slot="prefix" />
                Пользователи
              </SideNavItem>
              <SideNav onCollapsedChanged={(event) => isCollapsed.value = event.detail.value }
                       style={{ width: '100%' }} collapsible collapsed={isCollapsed.value}>
                <span slot="label">Компоненты</span>
                <SideNavItem path={'/components/type'} key={'components_type_key'}>
                  <Icon icon="vaadin:compile" slot="prefix" />
                  Типы компонентов
                </SideNavItem>
                <SideNavItem path={'/components/name_set'} key={'components_name_set_key'}>
                  <Icon icon="vaadin:archives" slot="prefix" />
                  Набор компонентов
                </SideNavItem>
              </SideNav>
            </SideNav>
        </header>
      </div>

        <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h1 slot="navbar" style={{ width: '100%', alignItems: 'center' }} className="text-l m-0 flex">
        {documentTitleSignal}
        <div style={{ marginLeft: 'auto', marginRight: '2em' }}>
          <Button onClick={logout} theme="icon" aria-label="Close">
            <Icon icon="vaadin:close-small" />
            <Tooltip slot="tooltip" text="Выход" />
          </Button>
        </div>
      </h1>

      <Suspense fallback={"Loading..."}>
        <div className={"container_outer"}>
          <Outlet />
        </div>

      </Suspense>
    </AppLayout>
  );
}
