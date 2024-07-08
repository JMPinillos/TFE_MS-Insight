using Google.Protobuf.WellKnownTypes;
using MsInsightApi.Dtos;
using MsInsightApi.Models;
using MsInsightApi.Repositories;
using MsInsightApi.Repositories.Interfaces;
using MsInsightApi.Services.Interfaces;

namespace MsInsightApi.Services;

public class ScoresService(IViewsRepository viewsRepository) : IScoresService
{
    private readonly IViewsRepository _viewsRepository = viewsRepository;
    private IEnumerable<ViewFormsScore>? _formScores;

    public async Task<IEnumerable<int>> GetPatients()
    {
       var patients = await _viewsRepository.GetPatients();
       var patientsNumber = patients.Select(x => x.PatientId);
       
       return patientsNumber;
    }
    
    public async Task<ScoresGroupDto> GetScoresByPatientId(int patientId)
    {
        var fssScores = await _viewsRepository.GetFssScores(patientId);
        var haqScores = await _viewsRepository.GetHaqScores(patientId);
        var msis29Scores = await _viewsRepository.GetMsis29ScoresByPatient(patientId);
        var msQoL54Scores = await _viewsRepository.GetMsqoL54PhysicalAndMentalScores(patientId);
        var neuroQoLsfScores = await _viewsRepository.GetNeuroQoLsfScores(patientId);
        var neuroQoLcogScores = await _viewsRepository.GetNeuroQoLcogScores(patientId);
        var medicalConsultations = await _viewsRepository.GetMedicalConsultations(patientId);
        var patientData = await _viewsRepository.GetPatientData(patientId);

        _formScores = await _viewsRepository.GetFormsScores();
        
        // Se retorna un objeto de tipo ScoresGroupDto con los scores de las pruebas FSS, HAQ, MSIS29, NeuroQoLCOG, NeuroQoLSF y MSQoL54.
        return await Task.FromResult(new ScoresGroupDto {
            FSS = CreateTestInfoDto(fssScores, x => new TestHistoryDto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, Score = x.Score }),
            HAQ = CreateTestInfoDto(haqScores, x => new TestHistoryDto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, Score = x.Score }),
            MSIS29 = CreateTestInfoDto(msis29Scores, x => new TestHistoryDto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, Score = x.Score }),
            NeuroQoLCOG = CreateTestInfoDto(neuroQoLcogScores, x => new TestHistoryDto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, Score = x.Score }),
            NeuroQoLSF = CreateTestInfoDto(neuroQoLsfScores, x => new TestHistoryDto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, Score = x.Score }),
            MSQoL54 = CreateTestInfoMSQoL54Dto(msQoL54Scores, x => new TestHistoryMsQoL54Dto { PatientNumber = x.NumeroPaciente, TestName = x.Test, Completed = x.Completado, PhysicalHealth = x.SaludFisica, MentalHealth = x.SaludMental }),
            MedicalConsultations = medicalConsultations.Select(x => new MedicalConsultationDto { PatientId = x.PatientId, FormId = x.FormId, CompletedAt = x.CompletedAt, Value = x.Value, Title = x.Title}),
            PatientData = patientData.Select(x => new PatientDataDto { PatientNumber = x.PatientId, Gender = x.Gender,  BirthDate = x.BirthDate, OnsetSymptoms = x.OnsetSymptoms, Brain_MRI = x.BrainMri, Spinal_MRI = x.SpinalMri })
        });
    }
    
    private TestInfoDto CreateTestInfoDto<T>(IEnumerable<T> historical, Func<T, TestHistoryDto> transformFunc)
    {
        
        // Al que se llama lastTest es el último test que se ha hecho.
        var lastTest = historical.Select(transformFunc).FirstOrDefault();

        // Al que se llama secondLastTest es el penúltimo test que se ha hecho.
        var secondLastTest = historical.Select(transformFunc).Skip(1).FirstOrDefault();

        // Se recupera el formulario para obtener el valor mínimo y máximo de la prueba.
        var form = _formScores?.FirstOrDefault(x => x.Title == lastTest?.TestName);

        var percentageScore = 0.0;
        var evolution = 0;
        DateTime? dateOfLastTest = null;

        if (lastTest?.Completed != null)
        {
            dateOfLastTest = lastTest.Completed;

            // calcula el porcentaje de la puntuación en relación al máximo y mínimo de la prueba.
            if (lastTest?.Score != null && form?.MaximumScore != null && form.MinimumScore != null)
            {
                double totalRange = (double)form.MaximumScore - (double)form.MinimumScore;
                double diference = (double)lastTest.Score - (double)form.MinimumScore;
                percentageScore = ((double)diference / (double)totalRange) * 100;
            }
        }
        
        if (lastTest?.Score != null && secondLastTest?.Score != null)
        {
            var diference = (double)lastTest.Score - (double)secondLastTest.Score;         
            evolution = diference > 0 ? 1 : ( diference == 0 ? 0 : -1) ;
        }
        
        return new TestInfoDto
        {
            Title = lastTest?.TestName,
            Score = lastTest?.Score,
            Evolution = evolution,
            DateOfLastTest = dateOfLastTest,
            Historical = historical.Select(transformFunc),
            PercentageScore = percentageScore,
            MaximumScore = form?.MaximumScore,
            MinimumScore = form?.MinimumScore
        };
    }
    
    private TestInfoMSQoL54Dto CreateTestInfoMSQoL54Dto<T>(IEnumerable<T> historical, Func<T, TestHistoryMsQoL54Dto> transformFunc)
    {
        // Al que se llama lastTest es el último test que se ha hecho.
        var lastTest = historical.Select(transformFunc).FirstOrDefault();

        return new TestInfoMSQoL54Dto
        {
            Title = lastTest?.TestName,
            Historical = historical.Select(transformFunc)
        };
    }
}