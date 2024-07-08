import { AuthState } from "../../interfaces/AuthState"; // Importa la definición del tipo 'AuthState'
import { AuthAction, authTypes } from "./types"; // Importa los tipos de acciones y la interfaz del estado del contexto de autenticación

// Define el reducer del contexto de autenticación
export const AuthReducer = (
	state: AuthState,
	action: AuthAction
): AuthState => {

	// Evalúa el tipo de acción
	switch (action.type) {
		case authTypes.login:
			// Devuelve el nuevo estado
			return {
				...state,
				logged: true,
				user: action.payload ? action.payload : state.user,
			};
		case authTypes.logout:
			// Devuelve el nuevo estado
			return {
				user: null,
				logged: false,
			};
		default:
			// Devuelve el estado actual
			return state;
	}
};
