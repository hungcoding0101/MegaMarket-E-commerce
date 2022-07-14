import React from 'react';
import ReactDOM from "react-dom/client";
import './index.css';
import App from './App';
import axios from 'axios';
import reportWebVitals from './reportWebVitals';
import { QueryClient, QueryClientProvider } from 'react-query';
import tokenUpdater from './Util/updateAuthToken';
import { CookiesProvider } from "react-cookie";

const retryFunc = (failureCount, error) => {
        console.log(
          `RETRY WITH ERROR: ${JSON.stringify(error.response.status)}`
        );
        if(failureCount < 1 && error.response.status === 401){
              console.log(`COUNT: ${failureCount}`)
              tokenUpdater.updateToken();  
              return true;
        }
        return false;
      }

/*Proxy for axios requests */
axios.defaults.baseURL = "";
axios.defaults.withCredentials = true;

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => retryFunc(failureCount, error),
      retryDelay: 1000,
    },

    mutations: {
      retry: (failureCount, error) => retryFunc(failureCount, error),
      retryDelay: 1000,
    },
  },
});

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <CookiesProvider>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </CookiesProvider>
);

reportWebVitals();
