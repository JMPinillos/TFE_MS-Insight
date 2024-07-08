// Define la interfaz IFileRepository con los métodos necesarios para un repositorio de archivos
export interface IFileRepository {
  uploadFixedData (formData: FormData): Promise<string>; // Método para subir datos fijos, devuelve la URL del archivo subido
  uploadVariableData (formData: FormData): Promise<string>; // Método para subir datos variables, devuelve la URL del archivo subido
}
  