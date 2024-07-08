namespace MsInsightApi.Dtos;

public class TestHistoryMsQoL54Dto
{
    public int PatientNumber { get; set; }
    public string TestName { get; set; } = null!;
    public DateTime Completed { get; set; }
    public double? PhysicalHealth { get; set; }
    public double? MentalHealth { get; set; }
}
