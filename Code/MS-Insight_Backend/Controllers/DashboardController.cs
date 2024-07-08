using Microsoft.AspNetCore.Mvc;
using MsInsightApi.Dtos;
using MsInsightApi.Services.Interfaces;

namespace MsInsightApi.Controllers;

[ApiController]
[Route("[controller]")]
public class DashboardController(IScoresService scoresService) : ControllerBase
{
    private readonly IScoresService _scoresService = scoresService;

    [HttpGet("patients", Name = "Patients")]
    public async Task<ActionResult<IEnumerable<int>>> GetPatients()
    {
        return  Ok(await _scoresService.GetPatients());
    }

    [HttpGet("scores/{patientId}", Name = "Scores")]
    public async Task<ActionResult<ScoresGroupDto>> GetScores(int patientId)
    {
        return Ok(await _scoresService.GetScoresByPatientId(patientId));
    }
    
}