import axios from "axios";
import { ScoresGroupDto } from "../../../types";
import { IScoresRepositorty } from "../IScoresRepository";
const API_URL = import.meta.env.VITE_NET_API_URL;


export class ScoresRepository implements IScoresRepositorty {
    // Devuelve los ids de los pacientes con información de los test completa
    async getPatients(): Promise<number[]> {
        try {
            const response = await axios.get<number[]>(`${API_URL}/Dashboard/patients`);
            return response.data;
          } catch (error) {
            console.error("Error al obtener los pacientes:", error);
            throw error;
          }
    }

    // Devuelve los scores de los diferentes tests
    async getScores(patientId: number): Promise<ScoresGroupDto> {
        try {
            const response = await axios.get<ScoresGroupDto>(`${API_URL}/Dashboard/scores/${patientId}`);
            return response.data;
          } catch (error) {
            console.error(`Error al obtener los scores del paciente ${patientId}:`, error);
            throw error;
          }
    }

}