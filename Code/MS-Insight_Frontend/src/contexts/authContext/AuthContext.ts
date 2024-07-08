import { createContext } from "react"; // Importa la librería 'react'
import { AuthContextProps } from "./AuthContextProps"; // Importa la interfaz 'AuthContextProps' 

// Crea el contexto de autenticación
export const AuthContext = createContext<AuthContextProps>(
	{} as AuthContextProps
);
