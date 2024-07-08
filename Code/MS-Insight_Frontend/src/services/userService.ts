// userService.ts

import { UserLogged } from "../interfaces/UserLogged";
import { userRepository } from "../data/persistence/InjectionDependencyRepository";


// Encontrar un usuario por email y contraseña
export  const login = async (email: string, password: string): Promise<UserLogged | null> => {
  // En una función asíncrona
  
  const user = await userRepository.login(email, password);
  if (user) {
    // Guardamos en localStorage el usuario logueado
    localStorage.setItem("userLogged", JSON.stringify(user));

    return user;
  }
  return null;
};

export const loginOut = async () => {
  localStorage.removeItem("userLogged");
  await userRepository.signOut();
}

export const getUserLogged = (): UserLogged | null => {
  const item = localStorage.getItem("userLogged");

  return item ? JSON.parse(item) as UserLogged : null;

};
