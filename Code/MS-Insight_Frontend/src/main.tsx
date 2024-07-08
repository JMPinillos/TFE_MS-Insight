import ReactDOM from 'react-dom/client'; // Importando el renderizador de React
import '@aws-amplify/ui-react/styles.css';
import 'bootstrap/dist/css/bootstrap.min.css'; // Importando estilos CSS de Bootstrap para el diseño
import './styles/index.css'; // Importando estilos CSS personalizados
import { AuthProvider } from './contexts/authContext/AuthProvider.tsx'; // Importando el proveedor de contexto para la autenticación
// import { HashRouter } from 'react-router-dom'; // Importando HashRouter para manejar el enrutamiento en la aplicación
import { AppRouter } from './routes/AppRouter.tsx'; // Importando el router de la aplicación que define las rutas
import DarkModeToggle from './components/darkMode/DarkModeToggle.tsx'; // Importando el componente para alternar el modo oscuro
import { BrowserRouter } from 'react-router-dom';

// Inicializando la aplicación React y renderizándola en el elemento con ID 'root'
ReactDOM.createRoot(document.getElementById('root')!).render(
  <>
    {/* Componente para alternar el modo oscuro */}
    <DarkModeToggle />

    {/* Envuelve toda la aplicación con HashRouter y AuthProvider para gestionar el enrutamiento y el contexto de autenticación */}
    <BrowserRouter>
      <AuthProvider>
        {/* Definición de rutas y renderización de componentes basados en la ruta */}
        <AppRouter />
      </AuthProvider>
    </BrowserRouter>
  </>
);