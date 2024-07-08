import { useContext } from "react"; // Importa la librería 'react'
import { AuthContextProps } from "../contexts/authContext/AuthContextProps"; // Importa la interfaz 'AuthContextProps'
import { AuthContext } from "../contexts/authContext/AuthContext"; // Importa el contexto de autenticación

// Este es un hook personalizado para usar el contexto de autenticación
export const useAuthContext = (): AuthContextProps => {
    // Utiliza el hook useContext para acceder al contexto de autenticación (AuthContext)
    const { authState, doLogin, doLogout } = useContext(AuthContext);

    // Devuelve el estado de autenticación y las funciones para iniciar y cerrar sesión
    return { authState, doLogin, doLogout };
};
