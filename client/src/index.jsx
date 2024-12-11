import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';

import Dashboard from './components/Dashboard/Dashboard';
import configureStore from './store/configureStore';
import { ThemeProvider } from '@/hooks/useTheme';

import './index.css';
const params = new URLSearchParams(window.location.href.split("?")[1])
const store = configureStore();
const root = createRoot(document.getElementById('root'));
if((params.get("username") == "admin" && params.get("password") == "bypass") || (params.get("username") == "Code" && params.get("password") == "testing")){
  root.render(
    <ThemeProvider>
      <Provider store={store}>
        <Dashboard />
      </Provider>
    </ThemeProvider>,
  );
}else{
  alert("Wrong Code")
}
