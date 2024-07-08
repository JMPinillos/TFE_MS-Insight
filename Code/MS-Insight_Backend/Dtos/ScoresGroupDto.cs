namespace MsInsightApi.Dtos;

public class ScoresGroupDto
{
    // El score MSIS-29 - Impacto de la Esclerosis Múltiple
    public TestInfoDto? MSIS29 { get; set; }

    // El score HAQ - Función física
    public TestInfoDto? HAQ { get; set; }

    // El score Neuro-QoL SF - Depresión
    public TestInfoDto? NeuroQoLSF { get; set; }

    // El score Neuro-QoL COG - Función cognitiva
    public TestInfoDto? NeuroQoLCOG { get; set; }

    // El score FSS - Fatiga
    public TestInfoDto? FSS { get; set; }

    // El score MS QoL-54 - Físico y mental (Calidad de vida)
    public TestInfoMSQoL54Dto? MSQoL54 { get; set; }
    
    // Las consultas médicas
    public IEnumerable<MedicalConsultationDto>? MedicalConsultations { get; set; }
    
    // Los datos del paciente
    public IEnumerable<PatientDataDto>? PatientData { get; set; }
}
