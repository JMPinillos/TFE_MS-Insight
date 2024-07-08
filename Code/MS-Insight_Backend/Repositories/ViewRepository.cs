using Microsoft.EntityFrameworkCore;
using MsInsightApi.Models;
using MsInsightApi.Repositories.Interfaces;

namespace MsInsightApi.Repositories;
/// <summary>
/// Repositorio para poder accder a las vistas de la base de datos.
/// </summary>
/// <param name="msInsightContext"></param>
public class ViewRepository(MsInsightContext msInsightContext) : IViewsRepository
{
    private readonly MsInsightContext _msInsightContext = msInsightContext;

    /// <summary> Metodo para obtener los scores minimo y maximo de las pruebas de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <exception cref="NotImplementedException"></exception>
    public async Task<IEnumerable<ViewFormsScore>> GetFormsScores()
    {
       return await _msInsightContext.ViewFormsScores.ToListAsync();
    }
    
    /// <summary> Metodo para obtener los datos de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Retorna los datos de un paciente. </returns>
    public async Task<IEnumerable<ViewPatientsDatum>> GetPatientData(int patientId)
    {
        return await _msInsightContext.ViewPatientsData.Where(x => x.PatientId == patientId).ToListAsync();
    }
    
    /// <summary> Metodo para obtener todos los pacientes de la base de datos. </summary>
    /// <returns> Retorna una lista de pacientes. </returns>
    public async Task<IEnumerable<ViewPatient>> GetPatients()
    {
        return await _msInsightContext.ViewPatients.ToListAsync();
    }
    
    /// <summary> Metodo para obtener los scores de la prueba FSS de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba FSS del paciente </returns>
    public async Task<IEnumerable<ViewFssScore>> GetFssScores(int patientId)
    {
        return await _msInsightContext.ViewFssScores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    /// <summary> Metodo para obtener los scores de la prueba HAQ de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba HAQ del paciente< /returns>
    public async Task<IEnumerable<ViewHaqScore>> GetHaqScores(int patientId)
    {
        return await _msInsightContext.ViewHaqScores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    /// <summary> Metodo para obtener los scores de la prueba MSIS-29 de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba MSIS-29 del paciente </returns>
    public async Task<IEnumerable<ViewMsis29Score>> GetMsis29ScoresByPatient(int patientId)
    {
        return await _msInsightContext.ViewMsis29Scores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    /// <summary> Metodo para obtener los scores de la prueba MS QoL-54 de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba MS QoL-54 del paciente </returns>
    public async Task<IEnumerable<ViewMsqoL54PhysicalAndMentalScore>> GetMsqoL54PhysicalAndMentalScores(int patientId)
    {
        return await _msInsightContext.ViewMsqoL54PhysicalAndMentalScores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    /// <summary> Metodo para obtener los scores de la prueba Neuro-QoL SF de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba Neuro-QoL SF del paciente </returns>
    public async Task<IEnumerable<ViewNeuroQoLsfScore>> GetNeuroQoLsfScores(int patientId)
    {
        return await _msInsightContext.ViewNeuroQoLsfScores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    /// <summary> Metodo para obtener los scores de la prueba Neuro-QoL COG de un paciente. </summary>
    /// <param name="patientId"></param>
    /// <returns> Scores de la prueba Neuro-QoL COG del paciente </returns>
    public async Task<IEnumerable<ViewNeuroQoLcogScore>> GetNeuroQoLcogScores(int patientId)
    {
        return await _msInsightContext.ViewNeuroQoLcogScores.Where(x => x.NumeroPaciente == patientId).ToListAsync();
    }

    public async Task<IEnumerable<ViewMedicalConsultation>> GetMedicalConsultations(int patientId)
    {
        return await _msInsightContext.ViewMedicalConsultations.Where(x => x.PatientId == patientId).ToListAsync();
    }


}