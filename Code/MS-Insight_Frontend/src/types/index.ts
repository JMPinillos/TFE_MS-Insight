export interface ScoresGroupDto {
    msiS29: TestInfoDto;
    haq: TestInfoDto;
    neuroQoLSF: TestInfoDto;
    neuroQoLCOG: TestInfoDto;
    fss: TestInfoDto;
    msQoL54: TestInfoMsQoL54Dto;
    medicalConsultations: medicalConsultationsDto[] | null;
  }
  
export interface TestHistoryDto {
    patientNumber: number;
    completed: string; // ISO date format string
    score: number;
  }
  
export interface TestHistoryMsQoL54Dto {
    testName: string;
    patientNumber: number;
    completed: string; // ISO date format string
    physicalHealth: number;
    mentalHealth: number;
  }
  
export interface TestInfoDto {
    historical: TestHistoryDto[];
    title: string;
    percentageScore: number;
    minimumScore: number;
    maximumScore: number;
    score: number;
    evolution: number;
    dateOfLastTest: string | null; // ISO date format string
  }

  export interface TestInfoMsQoL54Dto {
    historical: TestHistoryMsQoL54Dto[];
    title: string;
  }

  export interface medicalConsultationsDto {
    patientId: number;
    formId: number;
    completedAt: string; // ISO date format string
    value: string;
    title: string;
  }

  // Roles definidos
  export enum RoleType {
    ADMIN = 'ADMIN',
    DOCTOR = 'DOCTOR',
    RESEARCHER = 'RESEARCHER',
  }
  