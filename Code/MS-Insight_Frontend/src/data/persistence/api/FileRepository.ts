import axios from "axios";
import { IFileRepository } from "../IFileRepository";
const API_URL = import.meta.env.VITE_NET_API_URL;

export class FileRepository implements IFileRepository {
    // Sube un archivo de datos fijos
    async uploadFixedData(formData: FormData): Promise<string> {
        try {

            const response = await axios.post(`${API_URL}/UploadFiles/uploadFixedData`, formData, {
                headers: {
                  'Content-Type': 'multipart/form-data'
                }
              });

              return response.data;

          } catch (error) {
            console.error("Error al subir los datos fijos:", error);
            throw error;
          }
    }

    // Sube un archivo de datos variables
    async uploadVariableData(formData: FormData): Promise<string> {
        try {

            const response = await axios.post(`${API_URL}/UploadFiles/uploadVariableData`, formData, {
                headers: {
                  'Content-Type': 'multipart/form-data'
                }
              });

              return response.data;

          } catch (error) {
            console.error("Error al subir los datos variables:", error);
            throw error;
          }
    }

}