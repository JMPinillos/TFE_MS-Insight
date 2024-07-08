import { UserLogged } from "../../interfaces/UserLogged"; // Importa la definición del tipo 'UserLogged'

// Define los tipos de acciones para el contexto de autenticación
export const authTypes = {
	login: "[Auth] Login",
	logout: "[Auth] Logout",
};

// Define la interfaz del estado del contexto de autenticación
export type AuthAction = {
	type: typeof authTypes.login | typeof authTypes.logout;
	payload?: UserLogged;
};
