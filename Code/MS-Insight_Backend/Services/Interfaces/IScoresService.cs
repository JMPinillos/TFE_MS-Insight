using MsInsightApi.Dtos;

namespace MsInsightApi.Services.Interfaces;

public interface IScoresService
{
    Task<IEnumerable<int>> GetPatients();
    Task<ScoresGroupDto> GetScoresByPatientId(int patientId);
    
}