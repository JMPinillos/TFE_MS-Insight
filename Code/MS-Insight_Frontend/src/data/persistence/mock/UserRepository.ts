import { UserLogged } from "../../../interfaces/UserLogged"; // Importa la definición del tipo 'UserLogged'
import { RoleType } from "../../../types";
import { IUserRepository } from "../IUserRepository"; // Importa la interfaz del repositorio de usuarios

// Se recuperan los tres perfiles posibles de usuario
const EMAIL_ADMIN = import.meta.env.VITE_EMAIL_ADMIN;
const EMAIL_RESEARCHER = import.meta.env.VITE_EMAIL_RESEARCHER;
const EMAIL_DOCTOR = import.meta.env.VITE_EMAIL_DOCTOR;
// Se recupera la contraseña de usuario, es la misma para todos los perfiles
const EMAIL_USER_PASS = import.meta.env.VITE_EMAIL_USER_PASS;




export class UserRepository implements IUserRepository {
    private users: UserLogged[] = [
        { id: '1', email: EMAIL_ADMIN, rol:RoleType.ADMIN, token:'Tokennn', name: 'Test Admin User' },
        { id: '2', email: EMAIL_RESEARCHER, rol:RoleType.RESEARCHER, token:'Tokennn', name: 'Test Researcher User' },
        { id: '3', email: EMAIL_DOCTOR, rol:RoleType.DOCTOR, token:'Tokennn', name: 'Test Doctor User' }
    ];

    

    async login(email: string, password: string): Promise<UserLogged | null> {
        const user = this.users.find(u => u.email === email);
        // Simulamos la autenticación exitosa si el usuario existe y la contraseña es 'password'
        return user && password === EMAIL_USER_PASS ? user : null;
    }

    async signOut(): Promise<boolean> {
        // Simulamos un cierre de sesión exitoso
        return true;
    }

    async getAvatarUrl(id: string): Promise<string | null> {
        // const user = this.users.find(u => u.id === id);
        // return user ? user.avatarUrl : null;
        console.log(id);
        return null;
    }
}