// Importa la interfaz UserLogged desde el archivo UserLogged
import { UserLogged } from "./UserLogged";

// Define la interfaz AuthState para el estado de autenticación
export interface AuthState {
	logged: boolean; // Propiedad 'logged': indica si el usuario está logueado o no
	user: UserLogged | null; // Propiedad 'user': contiene los datos del usuario logueado o null si no hay usuario logueado
}
