/******************************************************************************
 * This file is auto-generated by Vaadin.
 * If you want to customize the entry point, you can copy this file or create
 * your own `index.tsx` in your frontend directory.
 * By default, the `index.tsx` file should be in `./frontend/` folder.
 *
 * NOTE:
 *     - You need to restart the dev-server after adding the new `index.tsx` file.
 *       After that, all modifications to `index.tsx` are recompiled automatically.
 *     - `index.js` is also supported if you don't want to use TypeScript.
 ******************************************************************************/

import { createContext, createElement, Suspense } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './routes';
import {AuthProvider} from "Frontend/components/config/auth/auth";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ToastContainer } from 'react-toastify';
import { Loading } from 'Frontend/components/config/Loading';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import 'dayjs/locale/ru';
import createState, { StateApp } from 'Frontend/components/config/state';

const state = createState();
export const Context = createContext<StateApp>(state);

const queryClient = new QueryClient()

function App() {
    return (
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
            <Context.Provider value={state}>
                <Suspense fallback={<Loading />}>
                    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ru">
                        <RouterProvider router={router} />
                    </LocalizationProvider>
                </Suspense>
                <ToastContainer />
            </Context.Provider>
        </AuthProvider>
      </QueryClientProvider>
    );
}

const outlet = document.getElementById('outlet')!;
let root = (outlet as any)._root ?? createRoot(outlet);
(outlet as any)._root = root;
root.render(createElement(App));

