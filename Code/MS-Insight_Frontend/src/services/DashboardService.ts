// services/DashboardService.ts
import { ScoresGroupDto } from '../types';
import { scoresRepository } from '../data/persistence/InjectionDependencyRepository';

export const getPatients = async (): Promise<number[]> => {
  return await scoresRepository.getPatients();
};

export const getScores = async (patientId: number): Promise<ScoresGroupDto> => {
  return await scoresRepository.getScores(patientId);
};