// Objetivo: Inyección de dependencias de los repositorios
import { IScoresRepositorty } from "./IScoresRepository";
import { IUserRepository } from "./IUserRepository"; // Importa la interfaz del repositorio de usuarios
import { UserRepository } from "./mock/UserRepository"; // Importa la implementación concreta del repositorio de usuarios
import { ScoresRepository } from "./api/ScoresRepository";
import { IFileRepository } from "./IFileRepository";
import { FileRepository } from "./api/FileRepository";
import { ILambdaRepository } from "./ILambdaRepository";
import { LambdaRespository } from "./api/LamdaRepository";
// Se crea una instancia de la clase TaskRepository
export const userRepository: IUserRepository = new UserRepository(); // Se exporta la instancia de UserRepository
export const scoresRepository: IScoresRepositorty = new ScoresRepository(); // Se exporta la instancia de ScoresRepository
export const fileRepository: IFileRepository = new FileRepository(); // Se exporta la instancia de FileRepository
export const lambdaRespository: ILambdaRepository = new LambdaRespository(); // Se exporta la instancia de LambdaRespository
