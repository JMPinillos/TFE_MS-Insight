import { useReducer } from "react"; // Importando hook useReducer para manejar el estado de la autenticación
import { AuthState } from "../../interfaces/AuthState"; // Importando interfaz para el estado de la autenticación
import { UserLogged } from "../../interfaces/UserLogged"; // Importando interfaz para el usuario logueado
import { AuthContext } from "./AuthContext"; // Importando contexto para la autenticación
import { AuthReducer } from "./AuthReducer"; // Importando reducer para el estado de la autenticación
import { AuthAction, authTypes } from "./types"; // Importando tipos para el estado de la autenticación
import { getUserLogged } from "../../services/userService"; // Importando función para obtener el usuario logueado

// Definición de la interfaz para las propiedades del componente
interface AuthProviderProps {
	children: JSX.Element | JSX.Element[];
}

// Verifica si el usuario está logueado y si existe una sesión activa
const user = getUserLogged();
const initialState: AuthState = {
	logged: user ? true : false, // Estado inicial de autenticación, determina si el usuario está logueado
	user: user, // Información del usuario logueado
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
	const [authState, dispatch] = useReducer(AuthReducer, initialState); // Usa useReducer para manejar el estado de autenticación

	const doLogin = (userNew: UserLogged) => { // Función para manejar el inicio de sesión
		const user: UserLogged = {
			id: userNew.id,
			name: userNew.name,
			email: userNew.email,
			rol: userNew.rol,
			token: userNew.token,
		};

		let action: AuthAction = {
			type: authTypes.login, // Define el tipo de acción como 'login'
			payload: user, // Datos del usuario para la acción
		};

		dispatch(action); // Envía la acción al reducer
	};

	const doLogout = () => { // Función para manejar el cierre de sesión
		let action: AuthAction = {
			type: authTypes.logout, // Define el tipo de acción como 'logout'
			payload: undefined, // No se necesita payload para el logout
		};

		dispatch(action); // Envía la acción al reducer
	};

	return (
		<AuthContext.Provider
			value={{
				authState, // Estado actual de la autenticación
				doLogin, // Función para iniciar sesión
				doLogout, // Función para cerrar sesión
			}}
		>
			{children} {/* Renderiza los componentes hijos */}
		</AuthContext.Provider>
	);
};