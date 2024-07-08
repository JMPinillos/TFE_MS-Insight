namespace MsInsightApi.Dtos;

public class TestHistoryDto
{
    public  int PatientNumber { get; set; }
    public string TestName { get; set; } = null!;
    public DateTime Completed { get; set; }
    public double? Score { get; set; }
}
