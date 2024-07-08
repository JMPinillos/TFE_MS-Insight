import axios from "axios";
import { ILambdaRepository } from "../ILambdaRepository";
const API_URL = import.meta.env.VITE_NET_API_URL;

export class LambdaRespository implements ILambdaRepository {
    // Activa Landa
    async activate(): Promise<string> {
        try {
            const response = await axios.post(`${API_URL}/LambdaActivation/checkAndActivateLambda`, null, {
                headers: {
                  'Content-Type': 'multipart/form-data'
                }
              });

              return response.data;

          } catch (error) {
            console.error("Error al activar Landa:", error);
            throw error;
          }
    }

}