namespace MsInsightApi.Dtos;

public class MedicalConsultationDto
{
    public int PatientId { get; set; }
    public int FormId { get; set; }
    public DateTime CompletedAt { get; set; }
    public string Value { get; set; } = null!;
    public string Title { get; set; } = null !;
}