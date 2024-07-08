// Define la interfaz UserLogged para representar un usuario que ha iniciado sesión
export interface UserLogged {
    id: string; // Propiedad 'id': identificador único del usuario
    name: string; // Propiedad 'name': nombre del usuario
    email: string; // Propiedad 'email': correo electrónico del usuario
    rol: string; // Propiedad 'rol': rol o función del usuario dentro de la aplicación
    token: string; // Propiedad 'token': token de autenticación utilizado para la sesión del usuario
}
