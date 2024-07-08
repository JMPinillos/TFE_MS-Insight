import { useState } from "react"; // Importa la librería 'react'
import { useAuthContext } from "./useAuthContext"; // Importa el hook personalizado useAuthContext
import { login } from "../services/userService"; // Importa la función login del servicio de usuario

// Hook personalizado para controlar el inicio de sesión
export function useLoginController() {
    const { doLogin } = useAuthContext(); // Usa el hook useAuthContext para acceder a la función doLogin
    const [password, setPassword] = useState<string>(""); // Estado para guardar la contraseña
    const [email, setEmail] = useState<string>(""); // Estado para guardar el correo electrónico
    const [loginError, setLoginError] = useState<string>(""); // Estado para manejar los errores de inicio de sesión

    // Función para manejar el inicio de sesión del usuario
    const handleLoginUser = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault(); // Previene el comportamiento por defecto del formulario

        try {
            const user =  await login(email, password); // Intenta iniciar sesión con el correo electrónico y la contraseña
            if (user) {
                doLogin(user); // Si el usuario existe, llama a doLogin con los datos del usuario
            } else {
                setLoginError("El usuario no existe con esas credenciales"); // Si no, establece un mensaje de error
            }
        } catch (error) {
            setLoginError("El usuario no existe con esas credenciales. Error."); // Captura y maneja los errores durante el inicio de sesión
        }
    };

    // Devuelve los estados y funciones para ser usados en componentes
    return { 
        handleLoginUser, // Función para manejar el inicio de sesión
        password, // Estado de la contraseña
        setPassword, // Función para actualizar la contraseña
        email, // Estado del correo electrónico
        setEmail, // Función para actualizar el correo electrónico
        loginError, // Estado del error de inicio de sesión
        setLoginError // Función para actualizar el error de inicio de sesión
    };
}
