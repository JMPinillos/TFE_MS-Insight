export interface ILambdaRepository {
    activate (): Promise<string>; // Método para activar Landa, devuelve la URL del archivo subido
}
    