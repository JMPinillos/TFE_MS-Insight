namespace MsInsightApi.Dtos;

public class TestInfoDto
{

    public IEnumerable<TestHistoryDto>? Historical { get; set; }
    public string? Title { get; set; }
    public double? Score { get; set; }
    public int? Evolution { get; set; }
    public DateTime? DateOfLastTest { get; set; }
    public double? PercentageScore { get; set; }
    public double? MinimumScore { get; set; }
    public double? MaximumScore { get; set; }

}