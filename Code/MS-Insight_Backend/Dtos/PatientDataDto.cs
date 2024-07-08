namespace MsInsightApi.Dtos;

public class PatientDataDto
{
    public int PatientNumber { get; set; }
    public string Gender { get; set; }
    public DateTime BirthDate { get; set; }
    public DateTime OnsetSymptoms { get; set; }
    public string Brain_MRI { get; set; }
    public string Spinal_MRI { get; set; }
}