import { UserLogged } from "../../interfaces/UserLogged"; // Importa la definición del tipo 'UserLogged'


// Define la interfaz IUserRepository con los métodos necesarios para un repositorio de usuarios
export interface IUserRepository {
    login(email: string, password: string): Promise<UserLogged | null>; // Método para iniciar sesión, devuelve el usuario logueado o null si no se pudo iniciar sesión
    signOut(): Promise<boolean>; // Método para cerrar sesión, devuelve un booleano indicando éxito o fracaso
    getAvatarUrl(id: string): Promise<string | null>; // Método para obtener la URL del avatar de un usuario, devuelve la URL o null si no se encontró
  }
  