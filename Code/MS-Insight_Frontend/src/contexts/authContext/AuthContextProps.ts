import { UserLogged } from "../../interfaces/UserLogged"; // Importa la definición del tipo 'UserLogged'
import { AuthState } from "../../interfaces/AuthState"; // Importa la definición del tipo 'AuthState'

// Define la interfaz del contexto de autenticación
export type AuthContextProps = {
	authState: AuthState; // Define el estado del contexto de autenticación
	doLogin: (userLogged: UserLogged) => void; // Define el método para iniciar sesión
	doLogout: () => void; // Define el método para cerrar sesión
};
