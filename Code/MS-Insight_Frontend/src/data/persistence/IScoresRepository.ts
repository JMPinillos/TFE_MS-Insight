import { ScoresGroupDto } from "../../types";

export interface IScoresRepositorty {
    getPatients(): Promise<number[]>
    getScores(patientId: number): Promise<ScoresGroupDto>
}